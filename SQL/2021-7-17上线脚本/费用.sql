ALTER TABLE `order_receivable_cost`
ADD COLUMN `unit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位CODE' AFTER `is_internal`,
ADD COLUMN `unit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位名称' AFTER `unit_code`;


ALTER TABLE `order_payment_cost`
ADD COLUMN `unit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位CODE' AFTER `internal_department_id`,
ADD COLUMN `unit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位名称' AFTER `unit_code`;