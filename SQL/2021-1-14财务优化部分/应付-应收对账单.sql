-- 2021-1-14 李达荣:出账单增加法人主体id和结算单位code
ALTER TABLE `order_receivable_bill`
ADD COLUMN `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id' AFTER `updated_time`,
ADD COLUMN `unit_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算代码(customer_info)' AFTER `legal_entity_id`;

ALTER TABLE `order_payment_bill`
ADD COLUMN `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id' AFTER `updated_time`,
ADD COLUMN `supplier_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商代码(supplier_info)' AFTER `legal_entity_id`;