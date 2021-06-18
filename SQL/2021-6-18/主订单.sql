ALTER TABLE `order_info`
ADD COLUMN `operation_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间' AFTER `create_user_type`;