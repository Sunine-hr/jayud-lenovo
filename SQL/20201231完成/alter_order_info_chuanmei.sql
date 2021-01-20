--20201203 二期优化
ALTER TABLE `order_info`
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '法人主体ID' AFTER `legal_name`;