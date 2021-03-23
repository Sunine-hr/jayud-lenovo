-- 增加监管方式
ALTER TABLE `order_customs`
ADD COLUMN `supervision_mode` varchar(255) NULL COMMENT '监管方式' AFTER `updated_time`;

