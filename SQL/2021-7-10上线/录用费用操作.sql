ALTER TABLE `order_receivable_cost`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `legal_id`,
ADD COLUMN `is_internal` tinyint(2) NULL COMMENT '是否内部往来费用' AFTER `department_id`;



ALTER TABLE `order_payment_cost`
ADD COLUMN `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id' AFTER `supplier_id`,
ADD COLUMN `is_internal` tinyint(2) NULL COMMENT '是否内部往来费用' AFTER `department_id`;


ALTER TABLE `order_receivable_cost`
MODIFY COLUMN `is_internal` tinyint(2) NULL DEFAULT 0 COMMENT '是否内部往来费用' AFTER `department_id`;

ALTER TABLE `order_payment_cost`
MODIFY COLUMN `is_internal` tinyint(2) NULL DEFAULT 0 COMMENT '是否内部往来费用' AFTER `department_id`;