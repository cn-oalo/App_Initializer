package cn.oalo.infrastructure.repository;

import cn.oalo.domain.entity.UserRoleEntity;
import cn.oalo.domain.repository.UserRoleRepository;
import cn.oalo.infrastructure.converter.UserRoleConverter;
import cn.oalo.infrastructure.mapper.UserRoleMapper;
import cn.oalo.infrastructure.po.UserRolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户角色关联仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleRepository {
    
    private final UserRoleMapper userRoleMapper;
    
    @Override
    public Optional<UserRoleEntity> findById(Long id) {
        UserRolePO userRolePO = userRoleMapper.selectById(id);
        return Optional.ofNullable(userRolePO).map(UserRoleConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Optional<UserRoleEntity> findByUserIdAndRoleId(Long userId, Long roleId) {
        UserRolePO userRolePO = userRoleMapper.selectByUserIdAndRoleId(userId, roleId);
        return Optional.ofNullable(userRolePO).map(UserRoleConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Long save(UserRoleEntity userRole) {
        UserRolePO userRolePO = UserRoleConverter.INSTANCE.toPO(userRole);
        userRoleMapper.insert(userRolePO);
        return userRolePO.getId();
    }
    
    @Override
    public List<UserRoleEntity> findByUserId(Long userId) {
        List<UserRolePO> userRolePOList = userRoleMapper.selectByUserId(userId);
        return UserRoleConverter.INSTANCE.toEntityList(userRolePOList);
    }
    
    @Override
    public List<UserRoleEntity> findByRoleId(Long roleId) {
        List<UserRolePO> userRolePOList = userRoleMapper.selectByRoleId(roleId);
        return UserRoleConverter.INSTANCE.toEntityList(userRolePOList);
    }
    
    @Override
    public boolean deleteByUserId(Long userId) {
        int result = userRoleMapper.deleteByUserId(userId);
        return result > 0;
    }
    
    @Override
    public boolean deleteByRoleId(Long roleId) {
        int result = userRoleMapper.deleteByRoleId(roleId);
        return result > 0;
    }
    
    @Override
    public boolean deleteByUserIdAndRoleId(Long userId, Long roleId) {
        int result = userRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
        return result > 0;
    }
} 