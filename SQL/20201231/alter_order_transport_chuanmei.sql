--20201203 二期优化
ALTER TABLE `order_transport`
DROP COLUMN `encode`;

ALTER TABLE `order_transport`
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '法人主体ID' AFTER `legal_name`;
ALTER TABLE `order_transport`
ADD COLUMN `is_vehicle_weigh` boolean NOT NULL DEFAULT 1 COMMENT '是否车辆过磅' AFTER `need_input_cost`;