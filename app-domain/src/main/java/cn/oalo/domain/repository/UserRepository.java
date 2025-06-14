package cn.oalo.domain.repository;

import cn.oalo.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓库接口
 */
public interface UserRepository {
    
    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    Optional<UserEntity> findById(Long id);
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * 保存用户
     *
     * @param user 用户实体
     * @return 用户ID
     */
    Long save(UserEntity user);
    
    /**
     * 更新用户
     *
     * @param user 用户实体
     * @return 是否成功
     */
    boolean update(UserEntity user);
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteById(Long id);
    
    /**
     * 分页查询用户
     *
     * @param offset 偏移量
     * @param limit  限制
     * @return 用户列表
     */
    List<UserEntity> findByPage(Integer offset, Integer limit);
    
    /**
     * 查询用户总数
     *
     * @return 总数
     */
    Long count();
} 