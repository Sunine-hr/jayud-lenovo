ALTER TABLE `cost_info`
ADD COLUMN `is_reimbursement` tinyint(1) NULL COMMENT '实报实销' AFTER `is_driver_show`;