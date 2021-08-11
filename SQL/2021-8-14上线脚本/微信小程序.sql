--  司机增加微信小程序id
ALTER TABLE `driver_info`
ADD COLUMN `applet_id` varchar(50) NULL COMMENT '微信小程序id' AFTER `password`;

-- 中港地址增加经纬度
ALTER TABLE `delivery_address`
ADD COLUMN `lo_and_la` varchar(255) NULL COMMENT '经纬度' AFTER `mailbox`;


