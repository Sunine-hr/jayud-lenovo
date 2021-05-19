ALTER TABLE `order_payment_cost`
ADD COLUMN `sub_order_no` varchar(50) NULL COMMENT '子订单号' AFTER `unloading_address`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `sub_legal_name` varchar(50) NULL COMMENT '子订单法人主体姓名' AFTER `sub_order_no`;

ALTER TABLE `order_payment_cost`
ADD COLUMN `legal_id` varchar(50) NULL COMMENT '子订单法人主体id' AFTER `sub_legal_name`;



ALTER TABLE `order_receivable_cost`
ADD COLUMN `sub_order_no` varchar(50) NULL COMMENT '子订单号' AFTER `unloading_address`;

ALTER TABLE `order_receivable_cost`
ADD COLUMN `sub_legal_name` varchar(50) NULL COMMENT '子订单法人主体姓名' AFTER `sub_order_no`;


ALTER TABLE `order_receivable_cost`
ADD COLUMN `legal_id` varchar(50) NULL COMMENT '子订单法人主体id' AFTER `sub_legal_name`;
