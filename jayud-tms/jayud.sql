

-- 2020年11月10日李达荣，功能描述：订单派车信息表增加司机id字段
ALTER TABLE `order_send_cars`
ADD COLUMN `driver_info_id` bigint(20) NULL DEFAULT NULL COMMENT '司机信息id（driver_info表）' AFTER `updated_user`;