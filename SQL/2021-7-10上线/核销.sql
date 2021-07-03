ALTER TABLE `cancel_after_verification`
ADD COLUMN `short_amount` bigint NULL COMMENT '短款金额' AFTER `created_time`;