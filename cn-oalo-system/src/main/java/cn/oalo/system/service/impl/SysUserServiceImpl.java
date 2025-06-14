package cn.oalo.system.service.impl;

import cn.oalo.system.entity.SysUser;
import cn.oalo.system.mapper.SysUserMapper;
import cn.oalo.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 系统用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SysUser getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Override
    public SysUser getUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(SysUser user) {
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置默认值
        user.setStatus(1);
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        // 如果密码不为空，则加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 不更新密码
            user.setPassword(null);
        }
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserById(Long userId) {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setDelFlag(1);
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }
} 