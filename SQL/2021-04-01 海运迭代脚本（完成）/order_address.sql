
-- 订单地址表增加送货人/发货人
ALTER TABLE `order_address`
ADD COLUMN `consignee` varchar(255) NULL COMMENT '送货人/发货人'