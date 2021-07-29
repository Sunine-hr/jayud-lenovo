ALTER TABLE `cancel_after_verification`
ADD COLUMN `local_exchange_rate` decimal(10, 4) NULL COMMENT ''本币汇率'' AFTER `short_amount`,
ADD COLUMN `short_local_amount` decimal(20, 4) NULL COMMENT ''短款本币金额'' AFTER `local_exchange_rate`;