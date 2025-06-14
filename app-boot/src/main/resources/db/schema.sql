-- 创建数据库
CREATE DATABASE IF NOT EXISTS enterprise_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE enterprise_app;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname`    VARCHAR(50)  NOT NULL COMMENT '昵称',
    `mobile`      VARCHAR(20)           DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)          DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(255)          DEFAULT NULL COMMENT '头像',
    `status`      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- 角色表
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '角色名称',
    `code`        VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` VARCHAR(255)         DEFAULT NULL COMMENT '角色描述',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT(20) NOT NULL COMMENT '角色ID',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- 插入默认数据
-- 插入管理员角色
INSERT INTO `role` (`name`, `code`, `description`)
VALUES ('管理员', 'ADMIN', '系统管理员');

-- 插入普通用户角色
INSERT INTO `role` (`name`, `code`, `description`)
VALUES ('普通用户', 'USER', '普通用户');

-- 插入管理员用户，密码：admin123
-- BCrypt加密后的密码
INSERT INTO `user` (`username`, `password`, `nickname`, `mobile`, `email`, `avatar`)
VALUES ('admin', '$2a$10$3OvT31PnJPI.4LiLJFiSueiCs6PyUeG8dhPaYpwJ8PJqMVUzV.0lS', '管理员', '13800138000',
        'admin@example.com', 'https://example.com/avatar.png');

-- 给管理员分配管理员角色
INSERT INTO `user_role` (`user_id`, `role_id`)
VALUES (1, 1); 