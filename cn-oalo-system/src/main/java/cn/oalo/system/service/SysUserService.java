package cn.oalo.system.service;

import cn.oalo.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    SysUser getUserById(Long userId);

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean registerUser(SysUser user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 结果
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    boolean deleteUserById(Long userId);
} 