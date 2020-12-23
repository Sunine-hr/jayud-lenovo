# SQL脚本
CREATE TABLE `yunbaoguan_kingdee_push_log` (
  `apply_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报关单号(18位)(主键)',
  `push_status_code` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '推送状态code(枚举)',
  `push_status_msg` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '推送状态msg(枚举)',
  `ip_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问方IP',
  `user_id` int(11) DEFAULT NULL COMMENT '访问用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`apply_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云报关到金蝶推送日志';