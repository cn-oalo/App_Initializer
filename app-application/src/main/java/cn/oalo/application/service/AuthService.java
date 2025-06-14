package cn.oalo.application.service;

import cn.oalo.application.dto.LoginDTO;
import cn.oalo.application.dto.LoginResultDTO;
import cn.oalo.application.dto.UserRegisterDTO;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginResultDTO login(LoginDTO loginDTO);
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    Long register(UserRegisterDTO registerDTO);
} 