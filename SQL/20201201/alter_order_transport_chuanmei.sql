--20201203 二期优化,优先级1
ALTER TABLE `order_transport`
DROP COLUMN `encode`;

ALTER TABLE `order_transport`
ADD COLUMN `is_vehicle_weigh` boolean NOT NULL DEFAULT 1 COMMENT '是否车辆过磅' AFTER `need_input_cost`;