
-- 2021-03-17,中港派车增加骑师id
ALTER TABLE `order_send_cars`
ADD COLUMN `jockey_id` bigint(20) NULL COMMENT '骑师id(司机信息id)' AFTER `updated_user`;


ALTER TABLE `order_send_cars`
ADD COLUMN `jockey` varchar(50) NULL COMMENT '骑师姓名' AFTER `jockey_id`;