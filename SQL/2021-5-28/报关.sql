
--  报关增加备注
ALTER TABLE `order_customs`
CHANGE COLUMN `remarks` `order_remarks` varchar(255)  DEFAULT NULL COMMENT '订单备注' AFTER `supervision_mode`;