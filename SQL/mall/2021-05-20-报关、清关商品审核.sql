SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`customs_clearance`
    ADD COLUMN `audit_status` int(10) NULL DEFAULT NULL COMMENT '审核状态(0待审核 1已审核 2已取消)' AFTER `specification`;

ALTER TABLE `jayud_shop`.`customs_clearance`
    ADD COLUMN `audit_user_id` int(10) NULL DEFAULT NULL COMMENT '审核用户id(system_user id)' AFTER `audit_status`;

ALTER TABLE `jayud_shop`.`customs_clearance`
    ADD COLUMN `audit_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核用户名(system_user name)' AFTER `audit_user_id`;

ALTER TABLE `jayud_shop`.`customs_data`
    ADD COLUMN `audit_status` int(10) NULL DEFAULT NULL COMMENT '审核状态(0待审核 1已审核 2已取消)' AFTER `specification`;

ALTER TABLE `jayud_shop`.`customs_data`
    ADD COLUMN `audit_user_id` int(10) NULL DEFAULT NULL COMMENT '审核用户id(system_user id)' AFTER `audit_status`;

ALTER TABLE `jayud_shop`.`customs_data`
    ADD COLUMN `audit_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核用户名(system_user name)' AFTER `audit_user_id`;

SET
FOREIGN_KEY_CHECKS=1;


SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`customs_clearance` MODIFY COLUMN `audit_status` int (10) NULL DEFAULT 0 COMMENT '审核状态(0待审核 1已审核 2已取消)' AFTER `specification`;

ALTER TABLE `jayud_shop`.`customs_data` MODIFY COLUMN `audit_status` int (10) NULL DEFAULT 0 COMMENT '审核状态(0待审核 1已审核 2已取消)' AFTER `specification`;

SET
FOREIGN_KEY_CHECKS=1;