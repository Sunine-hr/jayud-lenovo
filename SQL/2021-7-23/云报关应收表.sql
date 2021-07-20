ALTER TABLE `yunbaoguan_receivable_cost`
ADD COLUMN `is_complete` tinyint(2) NULL COMMENT '是否完成费用同步' AFTER `updated_time`;

ALTER TABLE `yunbaoguan_receivable_cost`
MODIFY COLUMN `is_complete` tinyint(2) NULL DEFAULT 0 COMMENT '是否完成费用同步' AFTER `updated_time`;