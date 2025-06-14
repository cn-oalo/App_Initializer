package cn.oalo.infrastructure.mapper;

import cn.oalo.infrastructure.po.UserPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPO> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户PO
     */
    UserPO selectByUsername(@Param("username") String username);

    /**
     * 分页查询用户列表
     *
     * @param offset 偏移量
     * @param limit  限制
     * @return 用户列表
     */
    List<UserPO> selectPage(@Param("offset") Integer offset, @Param("limit") Integer limit);
} 