CREATE TABLE `lock_order` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `start_time` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(10) DEFAULT '1' COMMENT '状态（0无效 1有效）',
  `type` tinyint(10) DEFAULT NULL COMMENT '类型 (0:应收,1:应付)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='锁单表';