package cn.oalo.domain.repository;

import cn.oalo.domain.entity.RoleEntity;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓库接口
 */
public interface RoleRepository {
    
    /**
     * 根据角色ID查询角色
     *
     * @param id 角色ID
     * @return 角色实体
     */
    Optional<RoleEntity> findById(Long id);
    
    /**
     * 根据角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色实体
     */
    Optional<RoleEntity> findByCode(String code);
    
    /**
     * 保存角色
     *
     * @param role 角色实体
     * @return 角色ID
     */
    Long save(RoleEntity role);
    
    /**
     * 更新角色
     *
     * @param role 角色实体
     * @return 是否成功
     */
    boolean update(RoleEntity role);
    
    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteById(Long id);
    
    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<RoleEntity> findAll();
    
    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> findByUserId(Long userId);
} 