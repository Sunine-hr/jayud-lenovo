--20201203 二期优化
ALTER TABLE `order_transport`
DROP COLUMN `encode`;

ALTER TABLE `order_transport`
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '法人主体ID' AFTER `legal_name`;