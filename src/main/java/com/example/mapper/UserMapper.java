package com.example.mapper;

import com.example.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 通过ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User selectById(Long id);
    
    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User selectByUsername(String username);
    
    /**
     * 创建用户
     *
     * @param user 用户
     * @return 影响行数
     */
    int insert(User user);
    
    /**
     * 更新用户
     *
     * @param user 用户
     * @return 影响行数
     */
    int update(User user);
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    /**
     * 查询用户列表
     *
     * @return 用户列表
     */
    List<User> selectList();
    
    /**
     * 分页查询用户列表
     *
     * @param offset 偏移量
     * @param limit  限制
     * @return 用户列表
     */
    List<User> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    /**
     * 查询用户总数
     *
     * @return 用户总数
     */
    Long selectCount();
} 