
--  报关增加备注
ALTER TABLE `order_customs`
ADD COLUMN `order_remarks` varchar(255)  DEFAULT NULL COMMENT '订单备注' AFTER `is_send_mail`;