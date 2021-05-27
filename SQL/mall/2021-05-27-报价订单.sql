SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`customer` MODIFY COLUMN `clearing_way` int (10) NULL DEFAULT 1 COMMENT '结算方式(1票结 2月结)' AFTER `pay_passwd`;

ALTER TABLE `jayud_shop`.`customer_goods`
    ADD COLUMN `is_valid` int(10) NULL DEFAULT 1 COMMENT '是否有效(0无效 1有效)' AFTER `lca_cid`;

ALTER TABLE `jayud_shop`.`customer_goods`
    ADD COLUMN `export_country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出口国家(country code)' AFTER `is_valid`;

ALTER TABLE `jayud_shop`.`customer_goods` MODIFY COLUMN `status` int (11) NULL DEFAULT 0 COMMENT '审核状态代码：1-审核通过，0-等待审核，-1-审核不通过' AFTER `description`;

ALTER TABLE `jayud_shop`.`offer_info`
    ADD COLUMN `sail_time_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开船日期备注' AFTER `remarks`;

ALTER TABLE `jayud_shop`.`order_info`
    ADD COLUMN `clearing_way` int(10) NULL DEFAULT NULL COMMENT '结算方式(1票结 2月结)' AFTER `after_status_name`;

ALTER TABLE `jayud_shop`.`quotation_template`
    ADD COLUMN `clearing_way` int(10) NULL DEFAULT 1 COMMENT '结算方式(1票结 2按客户的结算方式(客户表customer clearing_way))' AFTER `estimated_time_calc`;

SET
FOREIGN_KEY_CHECKS=1;
