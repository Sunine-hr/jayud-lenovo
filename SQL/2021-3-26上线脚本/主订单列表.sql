ALTER TABLE `order_info`
ADD COLUMN `is_rejected` tinyint(1) NULL COMMENT '是否驳回操作' AFTER `remarks`,
ADD COLUMN `reject_comment` varchar(500) NULL COMMENT '驳回描述' AFTER `isRejected`;




ALTER TABLE `order_info`
MODIFY COLUMN `is_rejected` tinyint(1) NULL DEFAULT 0 COMMENT '是否驳回操作' AFTER `remarks`;