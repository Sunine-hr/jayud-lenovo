-- 2021-2-3,车辆管理追加绑定司机id,增加主司机id
ALTER TABLE `vehicle_info`
ADD COLUMN `driver_info_ids` varchar(200) NULL COMMENT '司机id(多个用,隔开)' AFTER `file_name`,
ADD COLUMN `main_driver_id` bigint(20) NULL COMMENT '主司机id' AFTER `driver_info_ids`;


-- 2021-2-3,司机管理删除是否主司机,车辆id字段
ALTER TABLE `driver_info`
DROP COLUMN `is_main`,
DROP COLUMN `vehicle_id`;