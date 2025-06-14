package com.example.controller;

import com.example.common.api.PageQuery;
import com.example.common.api.PageResult;
import com.example.common.api.R;
import com.example.domain.entity.User;
import com.example.domain.vo.UserVO;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    @GetMapping("/me")
    public R<UserVO> getCurrentUser() {
        // 获取当前登录用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // 查询用户
        User user = userService.getUserByUsername(username);
        
        // 返回用户信息
        return R.ok(userService.getUserInfo(user.getId()));
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{id}")
    public R<UserVO> getUserInfo(@Parameter(description = "用户ID") @PathVariable Long id) {
        return R.ok(userService.getUserInfo(id));
    }

    /**
     * 分页查询用户列表
     *
     * @param query 查询参数
     * @return 用户列表
     */
    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public R<PageResult<UserVO>> listUsers(@Valid PageQuery query) {
        PageResult<UserVO> pageResult = userService.listUsersByPage(query.getPageNum(), query.getPageSize());
        return R.ok(pageResult);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public R<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        return R.ok(userService.deleteUser(id));
    }
} 