-- ----------------------------
-- 文件表结构
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名称',
  `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `file_md5` varchar(32) DEFAULT NULL COMMENT '文件MD5',
  `storage_type` tinyint(1) DEFAULT '0' COMMENT '存储类型（0本地 1阿里云OSS 2腾讯云COS 3七牛云Kodo 4MinIO）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1禁用）',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`file_id`),
  KEY `idx_file_name` (`file_name`),
  KEY `idx_file_md5` (`file_md5`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- ----------------------------
-- 菜单权限表新增文件管理菜单
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2000, '文件管理', 0, 5, 'file', NULL, 1, 0, 'M', '0', '0', '', 'file', 'admin', NOW(), '', NULL, '文件管理目录'),
(2001, '文件列表', 2000, 1, 'list', 'system/file/index', 1, 0, 'C', '0', '0', 'system:file:list', 'list', 'admin', NOW(), '', NULL, '文件列表菜单'),
(2002, '文件上传', 2001, 1, '', '', 1, 0, 'F', '0', '0', 'system:file:upload', '#', 'admin', NOW(), '', NULL, ''),
(2003, '文件下载', 2001, 2, '', '', 1, 0, 'F', '0', '0', 'system:file:download', '#', 'admin', NOW(), '', NULL, ''),
(2004, '文件删除', 2001, 3, '', '', 1, 0, 'F', '0', '0', 'system:file:delete', '#', 'admin', NOW(), '', NULL, '');

-- ----------------------------
-- 添加上传配置到系统配置表
-- ----------------------------
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(100, '文件上传路径', 'file.upload.path', 'upload', 'Y', 'admin', NOW(), '', NULL, '文件上传的基础路径'),
(101, '允许上传的文件类型', 'file.upload.allowed-types', '.jpg,.jpeg,.png,.gif,.bmp,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt,.zip,.rar,.gz,.7z,.mp3,.mp4,.avi,.rmvb', 'Y', 'admin', NOW(), '', NULL, '允许上传的文件类型，多个用逗号分隔'),
(102, '最大文件大小限制', 'file.upload.max-size', '10', 'Y', 'admin', NOW(), '', NULL, '最大文件大小限制，单位MB'),
(103, '文件存储类型', 'file.storage.type', 'LOCAL', 'Y', 'admin', NOW(), '', NULL, '文件存储类型（LOCAL本地 ALIYUN_OSS阿里云 TENCENT_COS腾讯云 QINIU_KODO七牛云 MINIO）'); 