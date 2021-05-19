ALTER TABLE `order_payment_cost`
ADD COLUMN `receivable_id` bigint(20) NULL COMMENT '应收费用id' AFTER `legal_id`;