package cn.oalo.infrastructure.repository;

import cn.oalo.domain.entity.RoleEntity;
import cn.oalo.domain.repository.RoleRepository;
import cn.oalo.infrastructure.converter.RoleConverter;
import cn.oalo.infrastructure.mapper.RoleMapper;
import cn.oalo.infrastructure.po.RolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {
    
    private final RoleMapper roleMapper;
    
    @Override
    public Optional<RoleEntity> findById(Long id) {
        RolePO rolePO = roleMapper.selectById(id);
        return Optional.ofNullable(rolePO).map(RoleConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Optional<RoleEntity> findByCode(String code) {
        RolePO rolePO = roleMapper.selectByCode(code);
        return Optional.ofNullable(rolePO).map(RoleConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Long save(RoleEntity role) {
        RolePO rolePO = RoleConverter.INSTANCE.toPO(role);
        roleMapper.insert(rolePO);
        return rolePO.getId();
    }
    
    @Override
    public boolean update(RoleEntity role) {
        RolePO rolePO = RoleConverter.INSTANCE.toPO(role);
        int result = roleMapper.updateById(rolePO);
        return result > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int result = roleMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public List<RoleEntity> findAll() {
        List<RolePO> rolePOList = roleMapper.selectList(null);
        return RoleConverter.INSTANCE.toEntityList(rolePOList);
    }
    
    @Override
    public List<RoleEntity> findByUserId(Long userId) {
        List<RolePO> rolePOList = roleMapper.selectByUserId(userId);
        return RoleConverter.INSTANCE.toEntityList(rolePOList);
    }
} 