ALTER TABLE `driver_employment_fee`
MODIFY COLUMN `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(0:待提交，1:已提交, 2:草稿)' AFTER `supplier_name`;