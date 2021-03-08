

ALTER TABLE `vehicle_info`
ADD COLUMN `type` int(10) NULL COMMENT '车辆类型(0:中港车,1:内陆车)' AFTER `main_driver_id`;