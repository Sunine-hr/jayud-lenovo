-- 2021-2-25 账单明细本币金额非必填
ALTER TABLE `order_payment_bill_detail`
MODIFY COLUMN `local_amount` decimal(10, 4) NULL COMMENT '费用类型/类别/名称维度的本币金额' AFTER `cost_id`;