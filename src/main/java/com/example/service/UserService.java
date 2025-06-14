package com.example.service;

import com.example.common.api.PageResult;
import com.example.domain.dto.RegisterDTO;
import com.example.domain.entity.User;
import com.example.domain.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 通过ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User getUserById(Long id);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户视图对象
     */
    UserVO getUserInfo(Long id);

    /**
     * 注册用户
     *
     * @param dto 注册信息
     * @return 用户ID
     */
    Long register(RegisterDTO dto);

    /**
     * 更新用户信息
     *
     * @param user 用户
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 分页查询用户
     *
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<UserVO> listUsersByPage(Integer pageNum, Integer pageSize);
} 