# Data structure synchronization
# 南京商城(mall) 数据结构同步 sql
# jayud_shop

SET FOREIGN_KEY_CHECKS=0;

-- 添加字段
ALTER TABLE `jayud_shop`.`order_info` ADD COLUMN `sales_commission` decimal(20, 2) NULL DEFAULT NULL COMMENT '销售提成' AFTER `extension_number`;

SET FOREIGN_KEY_CHECKS=1;

-- 修改表注释
ALTER TABLE `jayud_shop`.`counter_case` COMMENT = '货柜对应运单箱号信息-(删除表)';