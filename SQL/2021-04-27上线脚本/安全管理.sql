ALTER TABLE `system_user`
ADD COLUMN `update_pass_word_date` datetime NULL COMMENT '修改密码时间' AFTER `updated_user`;