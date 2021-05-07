SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`counter_case` COMMENT = '货柜对应运单箱号信息-(删除表)';

ALTER TABLE `jayud_shop`.`order_cope_receivable` ADD COLUMN `calculate_way` int(10) NULL DEFAULT 1 COMMENT '计算方式(1自动 2手动)' AFTER `cost_name`;

ALTER TABLE `jayud_shop`.`order_cope_receivable` ADD COLUMN `count` decimal(20, 4) NULL DEFAULT NULL COMMENT '数量' AFTER `calculate_way`;

ALTER TABLE `jayud_shop`.`order_cope_receivable` ADD COLUMN `unit_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '单价' AFTER `count`;

ALTER TABLE `jayud_shop`.`order_cope_with` ADD COLUMN `calculate_way` int(10) NULL DEFAULT 1 COMMENT '计算方式(1自动 2手动)' AFTER `supplier_id`;

ALTER TABLE `jayud_shop`.`order_cope_with` ADD COLUMN `count` decimal(10, 4) NULL DEFAULT NULL COMMENT '数量' AFTER `calculate_way`;

ALTER TABLE `jayud_shop`.`order_cope_with` ADD COLUMN `unit_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '单价' AFTER `count`;

ALTER TABLE `jayud_shop`.`quotation_template` ADD COLUMN `volume` decimal(20, 2) NULL DEFAULT NULL COMMENT '容量(数值)' AFTER `design_formulas`;

ALTER TABLE `jayud_shop`.`quotation_template` ADD COLUMN `volume_unit` int(10) NULL DEFAULT NULL COMMENT '容量单位(1KG 2CBM)' AFTER `volume`;

ALTER TABLE `jayud_shop`.`system_user` MODIFY COLUMN `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名(中文名)' AFTER `password`;

ALTER TABLE `jayud_shop`.`template_cope_receivable` ADD COLUMN `minimum_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '最小数量' AFTER `remarks`;

ALTER TABLE `jayud_shop`.`template_cope_with` ADD COLUMN `minimum_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '最小数量' AFTER `remarks`;

SET FOREIGN_KEY_CHECKS=1;