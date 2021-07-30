ALTER TABLE `order_info`
ADD COLUMN `is_complete` tinyint(2) NULL DEFAULT 0 COMMENT '是否完成操作' AFTER `operation_time`;