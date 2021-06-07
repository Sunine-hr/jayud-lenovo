-- 设置供应商车型
ALTER TABLE `order_transport`
ADD COLUMN `supplier_vehicle_size` varchar(255) NULL COMMENT '指派供应商吨位' AFTER `supplier_id`;