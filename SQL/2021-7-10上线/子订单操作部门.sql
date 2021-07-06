ALTER TABLE `order_transport`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `supplier_vehicle_size`;

ALTER TABLE `air_order`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `invoice_no`;

ALTER TABLE `order_customs`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `order_remarks`;

ALTER TABLE `order_inland_transport`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `update_time`;

ALTER TABLE `trailer_order`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `is_info_complete`;