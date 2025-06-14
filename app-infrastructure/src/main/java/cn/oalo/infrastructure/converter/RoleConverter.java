package cn.oalo.infrastructure.converter;

import cn.oalo.domain.entity.RoleEntity;
import cn.oalo.infrastructure.po.RolePO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色转换器
 */
@Mapper
public interface RoleConverter {
    
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);
    
    /**
     * PO转Entity
     *
     * @param po 持久化对象
     * @return 实体
     */
    RoleEntity toEntity(RolePO po);
    
    /**
     * Entity转PO
     *
     * @param entity 实体
     * @return 持久化对象
     */
    RolePO toPO(RoleEntity entity);
    
    /**
     * PO列表转Entity列表
     *
     * @param poList 持久化对象列表
     * @return 实体列表
     */
    List<RoleEntity> toEntityList(List<RolePO> poList);
    
    /**
     * Entity列表转PO列表
     *
     * @param entityList 实体列表
     * @return 持久化对象列表
     */
    List<RolePO> toPOList(List<RoleEntity> entityList);
} 