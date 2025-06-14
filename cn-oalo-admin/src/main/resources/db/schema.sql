-- 创建数据库
CREATE DATABASE IF NOT EXISTS `cn_oalo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `cn_oalo`;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户账号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- 菜单表
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) DEFAULT '0' COMMENT '是否为外链（0否 1是）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 用户和角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- 角色和菜单关联表
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- 产品表
DROP TABLE IF EXISTS `sys_product`;
CREATE TABLE `sys_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `product_name` varchar(100) NOT NULL COMMENT '产品名称',
  `product_code` varchar(50) NOT NULL COMMENT '产品编码',
  `category` varchar(50) NOT NULL COMMENT '产品分类',
  `price` decimal(10,2) NOT NULL COMMENT '产品价格',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存数量',
  `description` text COMMENT '产品描述',
  `image` varchar(255) DEFAULT NULL COMMENT '产品图片',
  `status` int(1) DEFAULT '1' COMMENT '状态(0:下架 1:上架)',
  `del_flag` int(1) DEFAULT '0' COMMENT '删除标志(0:未删除 1:已删除)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `idx_product_code` (`product_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='产品信息表';

-- 初始化-用户表数据
INSERT INTO sys_user (user_id, username, password, nick_name, status, create_by, create_time, remark)
VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '管理员', '0', 'admin', CURRENT_TIMESTAMP, '管理员');

-- 初始化-角色表数据
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, status, create_by, create_time, remark)
VALUES (1, '超级管理员', 'admin', 1, '0', 'admin', CURRENT_TIMESTAMP, '超级管理员');

-- 初始化-菜单表数据
-- 一级菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1, '系统管理', 0, 1, 'system', NULL, 0, 0, 'M', '0', '0', '', 'system', 'admin', CURRENT_TIMESTAMP, '系统管理目录');

-- 二级菜单
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', 0, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', CURRENT_TIMESTAMP, '用户管理菜单');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', 0, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', CURRENT_TIMESTAMP, '角色管理菜单');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', 0, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', CURRENT_TIMESTAMP, '菜单管理菜单');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (103, '产品管理', 1, 4, 'product', 'system/product/index', 0, 0, 'C', '0', '0', 'system:product:list', 'shopping', 'admin', CURRENT_TIMESTAMP, '产品管理菜单');

-- 用户管理按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1001, '用户查询', 100, 1, '', '', 0, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1002, '用户新增', 100, 2, '', '', 0, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1003, '用户修改', 100, 3, '', '', 0, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1004, '用户删除', 100, 4, '', '', 0, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', CURRENT_TIMESTAMP, '');

-- 产品管理按钮
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1031, '产品查询', 103, 1, '', '', 0, 0, 'F', '0', '0', 'system:product:query', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1032, '产品新增', 103, 2, '', '', 0, 0, 'F', '0', '0', 'system:product:add', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1033, '产品修改', 103, 3, '', '', 0, 0, 'F', '0', '0', 'system:product:edit', '#', 'admin', CURRENT_TIMESTAMP, '');
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES (1034, '产品删除', 103, 4, '', '', 0, 0, 'F', '0', '0', 'system:product:remove', '#', 'admin', CURRENT_TIMESTAMP, '');

-- 用户和角色关联表数据
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 角色和菜单关联表数据
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 100);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 101);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 102);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 103);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1001);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1002);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1003);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1004);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1031);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1032);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1033);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1034);

-- 初始化产品数据
INSERT INTO sys_product (product_id, product_name, product_code, category, price, stock, description, status, create_by, create_time)
VALUES (1, '笔记本电脑', 'PC001', '电子产品', 5999.00, 100, '高性能笔记本电脑', 1, 'admin', CURRENT_TIMESTAMP);
INSERT INTO sys_product (product_id, product_name, product_code, category, price, stock, description, status, create_by, create_time)
VALUES (2, '智能手机', 'PHONE001', '电子产品', 3999.00, 200, '最新款智能手机', 1, 'admin', CURRENT_TIMESTAMP);
INSERT INTO sys_product (product_id, product_name, product_code, category, price, stock, description, status, create_by, create_time)
VALUES (3, '办公椅', 'CHAIR001', '办公家具', 599.00, 50, '人体工学办公椅', 1, 'admin', CURRENT_TIMESTAMP); 