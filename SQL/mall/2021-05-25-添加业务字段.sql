SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `audit_status` int(10) NULL DEFAULT NULL COMMENT '审核状态(0待审核 1审核通过 2审核不通过)' AFTER `region_name`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `audit_user_id` int(10) NULL DEFAULT NULL COMMENT '审核用户id(system_user id)' AFTER `audit_status`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `audit_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核用户名(system_user name)' AFTER `audit_user_id`;

ALTER TABLE `jayud_shop`.`quotation_template`
    ADD COLUMN `cut_off_time_calc` int(10) NULL DEFAULT NULL COMMENT '截单日期(计算天数)' AFTER `minimum_quantity`;

ALTER TABLE `jayud_shop`.`quotation_template`
    ADD COLUMN `jc_time_calc` int(10) NULL DEFAULT NULL COMMENT '截仓日期(计算天数)' AFTER `cut_off_time_calc`;

ALTER TABLE `jayud_shop`.`quotation_template`
    ADD COLUMN `jkc_time_calc` int(10) NULL DEFAULT NULL COMMENT '截亏仓日期(计算天数)' AFTER `jc_time_calc`;

ALTER TABLE `jayud_shop`.`quotation_template`
    ADD COLUMN `estimated_time_calc` int(10) NULL DEFAULT NULL COMMENT '预计到达时间(计算天数)' AFTER `jkc_time_calc`;

ALTER TABLE `jayud_shop`.`service_group` MODIFY COLUMN `status` char (1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)' AFTER `describe`;

SET
FOREIGN_KEY_CHECKS=1;