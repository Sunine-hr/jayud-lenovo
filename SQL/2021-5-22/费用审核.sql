
ALTER TABLE `order_payment_cost`
ADD COLUMN `legal_name` varchar(50) NULL COMMENT '订单法人主体姓名' AFTER `unloading_address`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `legal_id` bigint(50) NULL COMMENT '订单法人主体id' AFTER `legal_name`;



ALTER TABLE `order_receivable_cost`
ADD COLUMN `legal_name` varchar(50) NULL COMMENT '订单法人主体姓名' AFTER `unloading_address`;


ALTER TABLE `order_receivable_cost`
ADD COLUMN `legal_id` bigint(50) NULL COMMENT '订单法人主体id' AFTER `legal_name`;
