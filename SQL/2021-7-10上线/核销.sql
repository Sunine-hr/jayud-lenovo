ALTER TABLE `cancel_after_verification`
ADD COLUMN `short_amount` decimal(20, 4) NULL DEFAULT NULL COMMENT '短款金额' AFTER `created_time`;