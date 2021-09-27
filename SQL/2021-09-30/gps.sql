ALTER TABLE `gps_positioning`
ADD COLUMN `total_mileage` double(20) NULL COMMENT '总里程碑' AFTER `gps_time`;

ALTER TABLE `gps_positioning`
ADD COLUMN `addr` varchar(255)  NULL DEFAULT NULL COMMENT '地理位置' AFTER `total_mileage`,
ADD COLUMN `mile` double(255, 0) NULL DEFAULT NULL COMMENT '行驶里程' AFTER `addr`,
ADD COLUMN `stop_long` varchar(50) NULL COMMENT '停车时长' AFTER `mile`;