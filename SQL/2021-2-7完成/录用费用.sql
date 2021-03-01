
-- 2021-2-7 录用费用暂存绑定账单编号
ALTER TABLE `order_receivable_cost`
ADD COLUMN `tmp_bill_no` varchar(50) NULL DEFAULT NULL COMMENT '暂存绑定账单编号' AFTER `created_user`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `tmp_bill_no` varchar(50) NULL DEFAULT NULL COMMENT '暂存绑定账单编号' AFTER `files`;

-- 2021-2-24 录用费用追加卸货地址
ALTER TABLE `order_receivable_cost`
ADD COLUMN `unloading_address` varchar(255) NULL COMMENT '卸货地址' AFTER `tmp_bill_no`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `unloading_address` varchar(255)  NULL DEFAULT NULL COMMENT '卸货地址' AFTER `tmp_bill_no`;


