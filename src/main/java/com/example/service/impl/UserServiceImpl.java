package com.example.service.impl;

import com.example.common.api.PageResult;
import com.example.common.exception.BusinessException;
import com.example.domain.dto.RegisterDTO;
import com.example.domain.entity.User;
import com.example.domain.vo.UserVO;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public UserVO getUserInfo(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterDTO dto) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(dto.getUsername());
        if (existingUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // 设置默认头像和状态
        user.setAvatar("https://example.com/default-avatar.png");
        user.setStatus(1); // 启用状态
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
        // 插入用户
        userMapper.insert(user);
        
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        // 设置更新时间
        user.setUpdateTime(LocalDateTime.now());
        
        return userMapper.update(user) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public PageResult<UserVO> listUsersByPage(Integer pageNum, Integer pageSize) {
        // 计算偏移量
        Integer offset = (pageNum - 1) * pageSize;
        
        // 查询用户列表
        List<User> users = userMapper.selectPage(offset, pageSize);
        
        // 查询总数
        Long total = userMapper.selectCount();
        
        // 转换为VO对象
        List<UserVO> voList = users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(voList, total, pageNum, pageSize);
    }

    /**
     * 将用户实体转换为视图对象
     *
     * @param user 用户实体
     * @return 用户视图对象
     */
    private UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        }
        
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        
        return vo;
    }
} 