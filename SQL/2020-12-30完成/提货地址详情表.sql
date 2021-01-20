## sql

-- 2020年12月4日李达荣，功能描述：提货地址详情表增加国家主键字段
ALTER TABLE `delivery_address`
ADD COLUMN `country` int(20) NULL COMMENT '国家主键' AFTER `type`;