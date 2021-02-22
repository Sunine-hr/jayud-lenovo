
-- 2021-2-22 空运表追加发票号字段
ALTER TABLE `air_order`
ADD COLUMN `invoice_no` varchar(500) NULL COMMENT '发票号(多个逗号隔开)' AFTER `receiving_orders_date`;