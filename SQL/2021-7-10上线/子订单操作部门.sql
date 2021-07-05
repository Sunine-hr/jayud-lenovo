ALTER TABLE `order_transport`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `supplier_vehicle_size`;


ALTER TABLE `air_order`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `invoice_no`;