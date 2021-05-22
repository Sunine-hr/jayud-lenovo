-- 指派供应商id
ALTER TABLE `order_transport`
ADD COLUMN `supplier_id` bigint(20) NULL COMMENT '指派供应商id' AFTER `create_user_type`;