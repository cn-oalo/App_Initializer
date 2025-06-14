package com.example.service.impl;

import com.example.common.exception.BusinessException;
import com.example.domain.dto.LoginDTO;
import com.example.domain.dto.RegisterDTO;
import com.example.domain.entity.User;
import com.example.domain.vo.LoginVO;
import com.example.domain.vo.UserVO;
import com.example.security.JwtTokenUtil;
import com.example.service.AuthService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
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
        User user = userService.getUserByUsername(userDetails.getUsername());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 构建返回结果
        return LoginVO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(24 * 60 * 60L) // 24小时
                .userInfo(userVO)
                .build();
    }

    @Override
    public Long register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        User existingUser = userService.getUserByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 注册用户
        return userService.register(registerDTO);
    }
} 