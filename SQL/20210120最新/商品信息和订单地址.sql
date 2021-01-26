-- 2021-1-26,李达荣:商品信息/订单地址增加订单号
ALTER TABLE `goods`
ADD COLUMN `order_no` varchar(500) DEFAULT NULL COMMENT '订单号' AFTER `business_type`;

ALTER TABLE `order_address`
ADD COLUMN `order_no` varchar(500) NULL COMMENT '订单号' AFTER `business_id`;