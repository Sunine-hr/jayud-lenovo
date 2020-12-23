ALTER TABLE `order_transport`
MODIFY COLUMN `vehicle_size` int(10) NULL DEFAULT NULL COMMENT '车型(1-3T 2-5t 3-8T 4-10T 5-12T 6-20GP 7-40GP 8-45GP)' AFTER `vehicle_type`;