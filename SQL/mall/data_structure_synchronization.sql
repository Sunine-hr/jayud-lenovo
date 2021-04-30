# Data structure synchronization
# 南京商城(mall) 数据结构同步 sql
# jayud_shop

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`order_info` ADD COLUMN `sales_commission` decimal(20, 2) NULL DEFAULT NULL COMMENT '销售提成' AFTER `extension_number`;

SET FOREIGN_KEY_CHECKS=1;