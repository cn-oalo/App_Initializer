package cn.oalo.application.service.impl;

import cn.oalo.application.dto.UserDTO;
import cn.oalo.application.service.UserService;
import cn.oalo.common.api.PageResult;
import cn.oalo.common.exception.BusinessException;
import cn.oalo.domain.entity.RoleEntity;
import cn.oalo.domain.entity.UserEntity;
import cn.oalo.domain.entity.UserRoleEntity;
import cn.oalo.domain.repository.RoleRepository;
import cn.oalo.domain.repository.UserRepository;
import cn.oalo.domain.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UsrServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    
    @Override
    public UserDTO getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        return convertToDTO(userEntity);
    }
    
    @Override
    public UserDTO getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        return convertToDTO(userEntity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer status) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        userEntity.setStatus(status);
        userEntity.setUpdateTime(LocalDateTime.now());
        
        return userRepository.update(userEntity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        // 删除用户角色关联
        userRoleRepository.deleteByUserId(id);
        // 删除用户
        return userRepository.deleteById(id);
    }
    
    @Override
    public PageResult<UserDTO> listUsersByPage(Integer pageNum, Integer pageSize) {
        // 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        
        // 查询用户列表
        List<UserEntity> userEntityList = userRepository.findByPage(offset, pageSize);
        
        // 查询总数
        Long total = userRepository.count();
        
        // 转换为DTO
        List<UserDTO> userDTOList = userEntityList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return PageResult.of(userDTOList, total, pageNum, pageSize);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 校验用户是否存在
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 校验角色是否存在
        for (Long roleId : roleIds) {
            roleRepository.findById(roleId)
                    .orElseThrow(() -> new BusinessException("角色不存在：" + roleId));
        }
        
        // 删除旧的角色关联
        userRoleRepository.deleteByUserId(userId);
        
        // 添加新的角色关联
        List<UserRoleEntity> userRoleEntityList = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userId);
            userRoleEntity.setRoleId(roleId);
            userRoleEntity.setCreateTime(LocalDateTime.now());
            userRoleEntityList.add(userRoleEntity);
            userRoleRepository.save(userRoleEntity);
        }
        
        return true;
    }
    
    /**
     * 将实体转换为DTO
     *
     * @param userEntity 用户实体
     * @return 用户DTO
     */
    private UserDTO convertToDTO(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userEntity, userDTO);
        
        return userDTO;
    }
} 