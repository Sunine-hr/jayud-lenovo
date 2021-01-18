
-- 2020年12月4日李达荣，功能描述：客户地址增加字段
ALTER TABLE `delivery_address`
ADD COLUMN `fax` varchar(20) DEFAULT NULL COMMENT '传真' AFTER `area`,
ADD COLUMN `mailbox` varchar(20) DEFAULT NULL COMMENT '邮箱' AFTER `fax`;