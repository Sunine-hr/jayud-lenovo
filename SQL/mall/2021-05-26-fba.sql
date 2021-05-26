SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`fab_warehouse` MODIFY COLUMN `audit_status` int (10) NULL DEFAULT 0 COMMENT '审核状态(0待审核 1审核通过 2审核不通过)' AFTER `region_name`;

SET
FOREIGN_KEY_CHECKS=1;