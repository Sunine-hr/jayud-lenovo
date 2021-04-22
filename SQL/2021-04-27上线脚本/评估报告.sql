ALTER TABLE `customs_questionnaire`
MODIFY COLUMN `status` int(5) NULL DEFAULT 0 COMMENT '状态(0:待经理审核,1:待总经理审核,2:审核通过,3.审核拒绝)' AFTER `expires_time`,
MODIFY COLUMN `is_edit` tinyint(2) NULL DEFAULT NULL COMMENT '是否可编辑(暂时没用)' AFTER `audit_opinion`;