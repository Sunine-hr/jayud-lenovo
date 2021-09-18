ALTER TABLE `msg_push_record`
ADD COLUMN `sub_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务模块' AFTER `title`;


ALTER TABLE `msg_push_record`
ADD COLUMN `opt_status` varchar(10) NULL DEFAULT 1 COMMENT '操作状态(1:未读,2:已读,3:删除)' AFTER `title`;
