ALTER TABLE `order_info`
ADD COLUMN `remarks` varchar(255) NULL COMMENT '备注' AFTER `legal_entity_id`;