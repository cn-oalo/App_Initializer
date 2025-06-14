-- 创建数据库
CREATE DATABASE IF NOT EXISTS enterprise_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE enterprise_db;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(50) NOT NULL COMMENT '角色名称',
  `code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- 插入默认角色数据
INSERT INTO `role` (`name`, `code`, `description`, `create_time`, `update_time`)
VALUES
('管理员', 'ADMIN', '系统管理员', NOW(), NOW()),
('用户', 'USER', '普通用户', NOW(), NOW());

-- 插入默认管理员用户，密码为：admin123（使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `mobile`, `email`, `avatar`, `status`, `create_time`, `update_time`)
VALUES
('admin', '$2a$10$UIQVQZOEfgOKQ7JGpQ8GYO7ixCNK3SOZ.AUYfZRC7kVDwR9pHqIMO', '超级管理员', '13800138000', 'admin@example.com', 'https://example.com/default-avatar.png', 1, NOW(), NOW());

-- 关联管理员和角色
INSERT INTO `user_role` (`user_id`, `role_id`, `create_time`)
VALUES
(1, 1, NOW()); 