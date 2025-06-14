package cn.oalo.infrastructure.converter;

import cn.oalo.domain.entity.UserEntity;
import cn.oalo.infrastructure.po.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户转换器
 */
@Mapper
public interface UserConverter {
    
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);
    
    /**
     * PO转Entity
     *
     * @param po 持久化对象
     * @return 实体
     */
    UserEntity toEntity(UserPO po);
    
    /**
     * Entity转PO
     *
     * @param entity 实体
     * @return 持久化对象
     */
    UserPO toPO(UserEntity entity);
    
    /**
     * PO列表转Entity列表
     *
     * @param poList 持久化对象列表
     * @return 实体列表
     */
    List<UserEntity> toEntityList(List<UserPO> poList);
    
    /**
     * Entity列表转PO列表
     *
     * @param entityList 实体列表
     * @return 持久化对象列表
     */
    List<UserPO> toPOList(List<UserEntity> entityList);
} 