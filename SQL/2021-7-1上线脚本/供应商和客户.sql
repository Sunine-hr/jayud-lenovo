ALTER TABLE `customer_info`
ADD COLUMN `file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提单文件路径(多个逗号隔开)' AFTER `is_advanced_certification`,
ADD COLUMN `file_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提单文件名称(多个逗号隔开)' AFTER `file_path`;


ALTER TABLE `supplier_info`
ADD COLUMN `file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提单文件路径(多个逗号隔开)' AFTER `is_advanced_certification`,
ADD COLUMN `file_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提单文件名称(多个逗号隔开)' AFTER `file_path`;