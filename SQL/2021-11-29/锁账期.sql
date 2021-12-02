ALTER TABLE `lock_order`
ADD COLUMN `model` int(2) NULL COMMENT '模式(1:锁账单,2:锁费用)' AFTER `update_time`;