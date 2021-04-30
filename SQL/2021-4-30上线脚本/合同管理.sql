ALTER TABLE `contract_info`
ADD COLUMN `remarks` varchar(255) NULL COMMENT '备注' AFTER `bind_id`;