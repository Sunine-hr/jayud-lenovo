ALTER TABLE `profit_statement`
ADD COLUMN `operation_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间' AFTER `class_code`;