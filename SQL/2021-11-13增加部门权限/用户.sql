ALTER TABLE `system_user`
ADD COLUMN `part_time_dep_id` varchar(50) NULL COMMENT '兼职部门(多个逗号隔开)' AFTER `update_pass_word_date`;