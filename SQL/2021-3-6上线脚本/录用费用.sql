
ALTER TABLE `order_receivable_cost`
MODIFY COLUMN `number` decimal(10, 4) NULL DEFAULT NULL COMMENT '数量' AFTER `unit_price`;


ALTER TABLE `order_payment_cost`
MODIFY COLUMN `number` decimal(10, 4) NULL DEFAULT NULL COMMENT '数量' AFTER `unit_price`;