
-- 2021-2-7 录用费用暂存绑定账单编号
ALTER TABLE `order_receivable_cost`
ADD COLUMN `tmp_bill_no` varchar(50) NULL DEFAULT NULL COMMENT '暂存绑定账单编号' AFTER `created_user`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `tmp_bill_no` varchar(50) NULL DEFAULT NULL COMMENT '暂存绑定账单编号' AFTER `files`;