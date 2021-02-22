-- 2021-2-22 菜单追加类型字段
ALTER TABLE `system_menu`
ADD COLUMN `type` varchar(50) NULL COMMENT '菜单类型(zgys,ky,bg,hy)' AFTER `updated_user`;