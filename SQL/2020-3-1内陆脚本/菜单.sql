ALTER TABLE `system_menu`
MODIFY COLUMN `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单类型(zgys,ky,bg,hy,nl)' AFTER `updated_user`;