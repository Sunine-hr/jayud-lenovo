

ALTER TABLE `vehicle_info`
ADD COLUMN `type` int(10) NULL COMMENT '车辆类型(0:中港车,1:内陆车)' AFTER `main_driver_id`;

ALTER TABLE `vehicle_info`
MODIFY COLUMN `vehicle_tonnage` varchar(50) DEFAULT NULL COMMENT '车型尺寸-3T\\5T\\8T\\10T\\12T' AFTER `enterprise_code`;