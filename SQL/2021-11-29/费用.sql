ALTER TABLE `order_payment_cost`
ADD COLUMN `is_reimbursement` tinyint(1) NULL COMMENT '是否实报实销' AFTER `driver_cost_id`;

ALTER TABLE `order_payment_cost`
MODIFY COLUMN `is_reimbursement` tinyint(1) NULL DEFAULT 0 COMMENT '是否实报实销' AFTER `driver_cost_id`;


ALTER TABLE `order_receivable_cost`
ADD COLUMN `is_reimbursement` tinyint(1) NULL DEFAULT 0 COMMENT '是否实报实销' AFTER `create_type`;