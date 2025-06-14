package cn.oalo.application.service.impl;

import cn.oalo.application.dto.LoginDTO;
import cn.oalo.application.dto.LoginResultDTO;
import cn.oalo.application.dto.UserDTO;
import cn.oalo.application.dto.UserRegisterDTO;
import cn.oalo.application.service.AuthService;
import cn.oalo.application.service.UserService;
import cn.oalo.common.exception.BusinessException;
import cn.oalo.domain.entity.UserEntity;
import cn.oalo.domain.repository.UserRepository;
import cn.oalo.infrastructure.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        // 进行身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        
        // 更新安全上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 生成JWT令牌
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);
        
        // 获取用户信息
        UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
        
        // 构建返回结果
        return LoginResultDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(24 * 60 * 60L) // 24小时
                .userInfo(userDTO)
                .build();
    }
    
    @Override
    public Long register(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        userRepository.findByUsername(registerDTO.getUsername())
                .ifPresent(user -> {
                    throw new BusinessException("用户名已存在");
                });
        
        // 创建用户实体
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(registerDTO, userEntity);
        
        // 加密密码
        userEntity.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        
        // 设置默认头像和状态
        userEntity.setAvatar("https://example.com/default-avatar.png");
        userEntity.setStatus(1); // 启用状态
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        userEntity.setCreateTime(now);
        userEntity.setUpdateTime(now);
        
        // 保存用户
        return userRepository.save(userEntity);
    }
} 