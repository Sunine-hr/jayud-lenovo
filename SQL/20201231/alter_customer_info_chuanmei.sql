--20201202 二期优化
ALTER TABLE `customer_info`
DROP COLUMN `unit_account`,
DROP COLUMN `invoice_code`,
DROP COLUMN `unit_code`;

ALTER TABLE `customer_info`
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '法人主体ID' AFTER `email`;