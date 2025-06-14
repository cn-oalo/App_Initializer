package cn.oalo.interfaces.controller;

import cn.oalo.application.dto.UserDTO;
import cn.oalo.application.service.UserService;
import cn.oalo.common.api.PageResult;
import cn.oalo.common.api.R;
import cn.oalo.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 用户控制器
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 获取当前用户信息
     *
     * @return 当前用户信息
     */
    @GetMapping("/current")
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息")
    public R<UserDTO> getCurrentUser() {
        // 从SecurityContextHolder中获取当前登录的用户名
        String username = SecurityUtils.getCurrentUsername();
        UserDTO userDTO = userService.getUserByUsername(username);
        return R.ok(userDTO);
    }
    
    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据用户ID获取用户信息", notes = "根据用户ID获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @PreAuthorize("hasRole('ADMIN')")
    public R<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return R.ok(userDTO);
    }
    
    /**
     * 分页查询用户列表
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 用户列表
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户列表", notes = "分页查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "Integer", paramType = "query")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public R<PageResult<UserDTO>> getUsersByPage(
            @RequestParam @Min(1) Integer pageNum,
            @RequestParam @Min(1) Integer pageSize) {
        PageResult<UserDTO> pageResult = userService.listUsersByPage(pageNum, pageSize);
        return R.ok(pageResult);
    }
    
    /**
     * 更新用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    @ApiOperation(value = "更新用户状态", notes = "更新用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "status", value = "状态：0-禁用，1-启用", required = true, dataType = "Integer", paramType = "query")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public R<Boolean> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = userService.updateStatus(id, status);
        return R.ok(success);
    }
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @PreAuthorize("hasRole('ADMIN')")
    public R<Boolean> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        return R.ok(success);
    }
    
    /**
     * 分配用户角色
     *
     * @param id      用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PostMapping("/{id}/roles")
    @ApiOperation(value = "分配用户角色", notes = "分配用户角色")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @PreAuthorize("hasRole('ADMIN')")
    public R<Boolean> assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        boolean success = userService.assignRoles(id, roleIds);
        return R.ok(success);
    }
} 