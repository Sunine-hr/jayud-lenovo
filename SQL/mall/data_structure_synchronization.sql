# Data structure synchronization
# 南京商城(mall) 数据结构同步 sql
# jayud_shop

SET FOREIGN_KEY_CHECKS=0;

-- 添加字段
ALTER TABLE `order_info` ADD COLUMN `sales_commission` decimal(20, 2) NULL DEFAULT NULL COMMENT '销售提成' AFTER `extension_number`;

SET FOREIGN_KEY_CHECKS=1;

-- 修改表注释
ALTER TABLE `counter_case` COMMENT = '货柜对应运单箱号信息-(删除表)';

-- 2021.05.06 修改表结构
ALTER TABLE `order_cope_with`
ADD COLUMN `count` decimal(10, 4) NULL COMMENT '数量' AFTER `supplier_id`,
ADD COLUMN `unit_price` decimal(20, 2) NULL COMMENT '单价' AFTER `count`;

ALTER TABLE `order_cope_receivable`
ADD COLUMN `count` decimal(20, 4) NULL COMMENT '数量' AFTER `cost_name`,
ADD COLUMN `unit_price` decimal(20, 2) NULL COMMENT '单价' AFTER `count`;

ALTER TABLE `order_cope_with`
ADD COLUMN `calculate_way` int(10) NULL DEFAULT 1 COMMENT '计算方式(1自动 2手动)' AFTER `supplier_id`;

ALTER TABLE `order_cope_receivable`
ADD COLUMN `calculate_way` int(10) NULL DEFAULT 1 COMMENT '计算方式(1自动 2手动)' AFTER `cost_name`;

-- 2021.05.07 修改表结构
ALTER TABLE `quotation_template`
ADD COLUMN `volume` decimal(20, 2) NULL COMMENT '容量(数值)' AFTER `design_formulas`,
ADD COLUMN `volume_unit` int(10) NULL COMMENT '容量单位(1KG 2CBM)' AFTER `volume`;

ALTER TABLE `template_cope_receivable`
ADD COLUMN `minimum_quantity` decimal(20, 2) NULL COMMENT '最小数量' AFTER `remarks`;

ALTER TABLE `template_cope_with`
ADD COLUMN `minimum_quantity` decimal(20, 2) NULL COMMENT '最小数量' AFTER `remarks`;
