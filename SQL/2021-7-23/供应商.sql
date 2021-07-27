ALTER TABLE `supplier_info`
ADD COLUMN `app_key` varchar(50) NULL COMMENT '对接GPS所需要的key值' AFTER `file_name`,
ADD COLUMN `gps_address` varchar(150) CHARACTER SET utf8mb4 NULL COMMENT '对接GPS公用路径前缀' AFTER `app_key`;