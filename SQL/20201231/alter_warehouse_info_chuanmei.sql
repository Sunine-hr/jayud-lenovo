ALTER TABLE `warehouse_info`
ADD COLUMN `audit_status` char(1) NULL DEFAULT '1' COMMENT '审核状态(1-待审核 2-审核通过 0-拒绝)' AFTER `status`,
ADD COLUMN `audit_comment` varchar(100) NULL COMMENT '审核意见' AFTER `audit_status`,
ADD COLUMN `is_virtual` tinyint(0) NULL COMMENT '是否虚拟仓' AFTER `audit_comment`;