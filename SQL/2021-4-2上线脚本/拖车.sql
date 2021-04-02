ALTER TABLE `trailer_order`
ADD COLUMN `is_info_complete` tinyint(2) NULL COMMENT '是否资料齐全' AFTER `process_description`;