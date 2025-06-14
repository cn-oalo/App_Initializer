package cn.oalo.infrastructure.config;

import cn.oalo.common.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus元数据处理器，用于自动填充创建时间、修改时间等字段
 */
@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("开始自动填充插入字段");
        // 创建时间
        strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // 更新时间
        strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 创建人
        String userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            strictInsertFill(metaObject, "createBy", () -> userId, String.class);
            strictInsertFill(metaObject, "updateBy", () -> userId, String.class);
        }
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("开始自动填充更新字段");
        // 更新时间
        strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 更新人
        String userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            strictUpdateFill(metaObject, "updateBy", () -> userId, String.class);
        }
    }
} 