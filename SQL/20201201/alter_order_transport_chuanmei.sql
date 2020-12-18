--20201203 二期优化,优先级1
ALTER TABLE `order_transport`
DROP COLUMN `encode`;

ALTER TABLE `order_transport`
ADD COLUMN `is_vehicle_weigh` boolean NOT NULL DEFAULT 1 COMMENT '是否车辆过磅' AFTER `need_input_cost`,
ADD COLUMN `take_file` varchar(1000) NULL COMMENT '提货文件' AFTER `is_vehicle_weigh`,
ADD COLUMN `take_file_name` varchar(1000) NULL COMMENT '提货文件文称' AFTER `take_file`,
ADD COLUMN `vehicle_id` bigint(20) NULL COMMENT '香港清关车辆ID' AFTER `clear_customs_no`;