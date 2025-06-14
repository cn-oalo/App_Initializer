package cn.oalo.infrastructure.repository;

import cn.oalo.domain.entity.UserEntity;
import cn.oalo.domain.repository.UserRepository;
import cn.oalo.infrastructure.converter.UserConverter;
import cn.oalo.infrastructure.mapper.UserMapper;
import cn.oalo.infrastructure.po.UserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓库实现类
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserMapper userMapper;
    
    @Override
    public Optional<UserEntity> findById(Long id) {
        UserPO userPO = userMapper.selectById(id);
        return Optional.ofNullable(userPO).map(UserConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        UserPO userPO = userMapper.selectByUsername(username);
        return Optional.ofNullable(userPO).map(UserConverter.INSTANCE::toEntity);
    }
    
    @Override
    public Long save(UserEntity user) {
        UserPO userPO = UserConverter.INSTANCE.toPO(user);
        userMapper.insert(userPO);
        return userPO.getId();
    }
    
    @Override
    public boolean update(UserEntity user) {
        UserPO userPO = UserConverter.INSTANCE.toPO(user);
        int result = userMapper.updateById(userPO);
        return result > 0;
    }
    
    @Override
    public boolean deleteById(Long id) {
        int result = userMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public List<UserEntity> findByPage(Integer offset, Integer limit) {
        List<UserPO> userPOList = userMapper.selectPage(offset, limit);
        return UserConverter.INSTANCE.toEntityList(userPOList);
    }
    
    @Override
    public Long count() {
        return userMapper.selectCount(null);
    }
} 