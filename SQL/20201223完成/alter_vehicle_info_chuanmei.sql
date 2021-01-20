ALTER TABLE `vehicle_info`
MODIFY COLUMN `vehicle_tonnage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆吨位-3T\\5T\\8T\\10T\\12T' AFTER `enterprise_code`;