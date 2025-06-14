package cn.oalo.infrastructure.security;

import cn.oalo.domain.entity.RoleEntity;
import cn.oalo.domain.entity.UserEntity;
import cn.oalo.domain.repository.RoleRepository;
import cn.oalo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserDetailsService实现类
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户名不存在：" + username));
        
        // 用户被禁用
        if (user.getStatus() != 1) {
            throw new UsernameNotFoundException("用户已被禁用");
        }
        
        // 查询用户角色
        List<RoleEntity> roles = roleRepository.findByUserId(user.getId());
        
        // 转换为Spring Security的权限
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                .collect(Collectors.toList());
        
        // 返回UserDetails
        return new User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
} 