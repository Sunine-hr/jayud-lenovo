ALTER TABLE `delivery_address`
MODIFY COLUMN `address` varchar(500) COMMENT '详细地址' AFTER `city_name`;