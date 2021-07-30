ALTER TABLE `order_info`
ADD COLUMN `is_complete` tinyint(1) NULL DEFAULT 0 COMMENT '标志' AFTER `operation_time`;