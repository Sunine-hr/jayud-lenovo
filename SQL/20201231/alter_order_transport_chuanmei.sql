--20201203 二期优化
ALTER TABLE `order_transport`
DROP COLUMN `encode`;

ALTER TABLE `order_transport`
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '法人主体ID' AFTER `legal_name`,
ADD COLUMN `hk_legal_id` bigint(20) NULL COMMENT '香港清关接单法人ID' AFTER `hk_unit_code`;