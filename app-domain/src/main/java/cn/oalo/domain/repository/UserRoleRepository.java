package cn.oalo.domain.repository;

import cn.oalo.domain.entity.UserRoleEntity;

import java.util.List;
import java.util.Optional;

/**
 * 用户角色关联仓库接口
 */
public interface UserRoleRepository {
    
    /**
     * 根据ID查询用户角色关联
     *
     * @param id ID
     * @return 用户角色关联实体
     */
    Optional<UserRoleEntity> findById(Long id);
    
    /**
     * 根据用户ID和角色ID查询用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 用户角色关联实体
     */
    Optional<UserRoleEntity> findByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * 保存用户角色关联
     *
     * @param userRole 用户角色关联实体
     * @return ID
     */
    Long save(UserRoleEntity userRole);
    
    /**
     * 根据用户ID查询用户角色关联列表
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRoleEntity> findByUserId(Long userId);
    
    /**
     * 根据角色ID查询用户角色关联列表
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    List<UserRoleEntity> findByRoleId(Long roleId);
    
    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteByUserId(Long userId);
    
    /**
     * 根据角色ID删除用户角色关联
     *
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteByRoleId(Long roleId);
    
    /**
     * 根据用户ID和角色ID删除用户角色关联
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean deleteByUserIdAndRoleId(Long userId, Long roleId);
} 