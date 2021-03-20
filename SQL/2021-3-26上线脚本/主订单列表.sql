ALTER TABLE `order_info`
ADD COLUMN `isRejected` tinyint(1) NULL COMMENT '是否驳回操作' AFTER `remarks`,
ADD COLUMN `rejection_desc` varchar(500) NULL COMMENT '驳回描述' AFTER `isRejected`;