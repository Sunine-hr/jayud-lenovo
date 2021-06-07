-- 指派供应商id
ALTER TABLE `order_transport`
ADD COLUMN `supplier_id` bigint(20) NULL COMMENT '指派供应商id' AFTER `create_user_type`;

-- 录用费用绑定供应商
ALTER TABLE `order_payment_cost`
ADD COLUMN `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商id' AFTER `receivable_id`;