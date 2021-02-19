-- 2021-2-7 出账订单维度的费用统计记录表增加当前币种,结算汇率
ALTER TABLE `order_bill_cost_total`
ADD COLUMN `current_currency_code` varchar(255) NULL COMMENT '当前币种' AFTER `money_type`,
ADD COLUMN `exchange_rate` decimal(10, 4) NULL COMMENT '结算汇率' AFTER `current_currency_code`;

-- 2021-2-19 出账订单维度的费用统计记录表,增加是否自定汇率
ALTER TABLE `order_bill_cost_total`
ADD COLUMN `is_custom_exchange_rate` tinyint(1) NULL COMMENT '是否自定汇率' AFTER `exchange_rate`;