ALTER TABLE `order_address`
ADD COLUMN `enter_warehouse_no` varchar(50)  NULL DEFAULT NULL COMMENT '入仓号,送货有' AFTER `file_name`;