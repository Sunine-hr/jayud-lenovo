ALTER TABLE `customer_info`
MODIFY COLUMN `audit_status` int(11) NULL DEFAULT NULL COMMENT '审核状态( 0-待客服审核 1-待财务审核 2-待总经办审核  3-草稿 10-通过 11-拒绝)' AFTER `yw_id`;