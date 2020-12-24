ALTER TABLE `order_take_adr`
ADD COLUMN `file` varchar(1000) NULL COMMENT '提货送货文件' AFTER `create_user`,
ADD COLUMN `file_name` varchar(1000) NULL COMMENT '提货送货文件名称' AFTER `file`;