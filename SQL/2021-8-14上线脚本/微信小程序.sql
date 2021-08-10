ALTER TABLE `driver_info`
ADD COLUMN `applet_id` varchar(50) NULL COMMENT '微信小程序id' AFTER `password`;