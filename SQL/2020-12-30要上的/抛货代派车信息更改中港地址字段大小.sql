ALTER TABLE `delivery_address`
MODIFY COLUMN `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址' AFTER `city_name`;