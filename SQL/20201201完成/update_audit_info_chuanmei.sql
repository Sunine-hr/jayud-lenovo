--20201127 财务功能-审核信息表
ALTER TABLE audit_info ADD COLUMN ext_unique_flag varchar(50) DEFAULT NULL COMMENT '被审核表的唯一标识,财务可用' AFTER ext_id;
ALTER TABLE audit_info MODIFY COLUMN `ext_id` bigint(20) NULL COMMENT '被审核表的ID' AFTER `id`;