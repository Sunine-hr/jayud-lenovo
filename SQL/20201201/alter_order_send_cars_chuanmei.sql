--20201203 二期优化,优先级1
ALTER TABLE `order_send_cars`
DROP COLUMN `encode`,
DROP COLUMN `encode_url`,
DROP COLUMN `encode_url_name`,
DROP COLUMN `warehouse_info_id`;

ALTER TABLE `order_send_cars`
ADD COLUMN `vehicle_id` bigint(20) NULL COMMENT '车辆ID' AFTER `driver_info_id`;