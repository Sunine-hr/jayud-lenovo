CREATE TABLE `training_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject` varchar(500) DEFAULT NULL COMMENT '培训主题',
  `training_start_time` datetime DEFAULT NULL COMMENT '培训开始时间',
  `training_end_time` datetime DEFAULT NULL COMMENT '培训结束时间',
  `training_location` varchar(255) DEFAULT NULL COMMENT '培训地点',
  `trainees` varchar(255) DEFAULT NULL COMMENT '培训对象(多个对象隔开)',
  `content` text COMMENT '培训内容',
  `file_path` varchar(500) DEFAULT NULL COMMENT '附件路径(多个逗号隔开)',
  `file_name` varchar(500) DEFAULT NULL COMMENT '附件名字(多个逗号隔开)',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='培训管理';