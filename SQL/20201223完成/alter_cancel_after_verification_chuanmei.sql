ALTER TABLE `cancel_after_verification`
ADD COLUMN `remarks` varchar(100) NULL COMMENT '备注' AFTER `local_money`;