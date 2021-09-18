ALTER TABLE `order_address`
ADD COLUMN `province` int(11) NULL DEFAULT NULL COMMENT '省主键' AFTER `consignee`,
ADD COLUMN `city` int(11) NULL DEFAULT NULL COMMENT '市主键' AFTER `province`,
ADD COLUMN `area` int(11) NULL DEFAULT NULL COMMENT '区主键' AFTER `city`;