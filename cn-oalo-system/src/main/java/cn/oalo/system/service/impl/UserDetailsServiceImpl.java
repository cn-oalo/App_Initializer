package cn.oalo.system.service.impl;


import cn.oalo.system.entity.SysUser;
import cn.oalo.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDetailsService实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @org.springframework.context.annotation.Lazy
    private SysUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询用户信息
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        
        // 用户被禁用
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用");
        }
        
        // 用户被删除
        if (user.getDelFlag() != null && user.getDelFlag() == 1) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 创建权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 如果用户有角色，添加角色权限
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
            });
        }
        
        // 返回UserDetails对象
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
} 