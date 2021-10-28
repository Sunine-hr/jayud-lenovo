CREATE TABLE `electronic_fence` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '围栏名称',
  `number` varchar(20) DEFAULT NULL COMMENT '围栏编号',
  `scope` double(20,2) NOT NULL COMMENT '范围',
  `status` int(5) NOT NULL DEFAULT '1' COMMENT '状态（0:禁用 1:启用 2:删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `province` bigint(11) DEFAULT NULL COMMENT '省主键',
  `city` bigint(11) DEFAULT NULL COMMENT '市主键',
  `area` bigint(11) DEFAULT NULL COMMENT '区主键',
  `addr` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `lo_and_la` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '经纬度(高德)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='电子围栏';