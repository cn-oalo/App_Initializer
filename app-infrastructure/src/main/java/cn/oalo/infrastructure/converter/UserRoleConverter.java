package cn.oalo.infrastructure.converter;

import cn.oalo.domain.entity.UserRoleEntity;
import cn.oalo.infrastructure.po.UserRolePO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户角色关联转换器
 */
@Mapper
public interface UserRoleConverter {
    
    UserRoleConverter INSTANCE = Mappers.getMapper(UserRoleConverter.class);
    
    /**
     * PO转Entity
     *
     * @param po 持久化对象
     * @return 实体
     */
    UserRoleEntity toEntity(UserRolePO po);
    
    /**
     * Entity转PO
     *
     * @param entity 实体
     * @return 持久化对象
     */
    UserRolePO toPO(UserRoleEntity entity);
    
    /**
     * PO列表转Entity列表
     *
     * @param poList 持久化对象列表
     * @return 实体列表
     */
    List<UserRoleEntity> toEntityList(List<UserRolePO> poList);
    
    /**
     * Entity列表转PO列表
     *
     * @param entityList 实体列表
     * @return 持久化对象列表
     */
    List<UserRolePO> toPOList(List<UserRoleEntity> entityList);
} 