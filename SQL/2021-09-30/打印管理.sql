CREATE TABLE `print_report_manage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `unique_iden` varchar(255) NOT NULL COMMENT '唯一标识',
  `template_url` varchar(255) NOT NULL COMMENT '模板地址',
  `params` varchar(255) DEFAULT NULL COMMENT '传入参数(多个用逗号隔开)',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `modular_name` varchar(255) DEFAULT NULL COMMENT '模块名称',
  `report_name` varchar(255) DEFAULT NULL COMMENT '报表名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0无效 1有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='打印报表管理';