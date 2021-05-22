ALTER TABLE `system_user`
MODIFY COLUMN `user_type` char(1)  DEFAULT NULL COMMENT '1-用户 2-客户 3-供应商' AFTER `audit_status`;