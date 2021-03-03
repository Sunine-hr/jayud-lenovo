ALTER TABLE `system_menu`
MODIFY COLUMN `type` varchar(50) DEFAULT NULL COMMENT '菜单类型(zgys,ky,bg,hy,nl)' AFTER `updated_user`;