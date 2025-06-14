package cn.oalo.interfaces.controller;

import cn.oalo.application.dto.LoginDTO;
import cn.oalo.application.dto.LoginResultDTO;
import cn.oalo.application.dto.UserRegisterDTO;
import cn.oalo.application.service.AuthService;
import cn.oalo.common.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public R<LoginResultDTO> login(@Validated @RequestBody LoginDTO loginDTO) {
        LoginResultDTO result = authService.login(loginDTO);
        return R.ok(result);
    }
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public R<Long> register(@Validated @RequestBody UserRegisterDTO registerDTO) {
        Long userId = authService.register(registerDTO);
        return R.ok(userId);
    }
} 