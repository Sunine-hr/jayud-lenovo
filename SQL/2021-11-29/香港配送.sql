ALTER TABLE `order_take_adr`
ADD COLUMN `is_hk_delivery` tinyint(1) NULL DEFAULT 0 COMMENT '是否香港配送' AFTER `file_name`;


ALTER TABLE `contract_quotation_details`
ADD COLUMN `weight_billing` decimal(20, 2) NULL COMMENT '重量计费/kg' AFTER `destination_id`,
ADD COLUMN `num_billing` decimal(20, 2) NULL COMMENT '件数计费/件' AFTER `weight_billing`,
ADD COLUMN `plate_num_billing` decimal(20, 2) NULL COMMENT '板数计费/版' AFTER `num_billing`,
ADD COLUMN `min_billing` decimal(20, 2) NULL COMMENT '最低计费' AFTER `plate_num_billing`;
