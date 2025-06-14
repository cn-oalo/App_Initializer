package cn.oalo.infrastructure.mapper;

import cn.oalo.infrastructure.po.RolePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {

    /**
     * 根据角色编码查询角色
     *
     * @param code 角色编码
     * @return 角色PO
     */
    RolePO selectByCode(@Param("code") String code);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RolePO> selectByUserId(@Param("userId") Long userId);
} 