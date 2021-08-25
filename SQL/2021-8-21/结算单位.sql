CREATE TABLE `customer_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `customer_id` bigint(20) NOT NULL COMMENT ''客户id'',
  `business_type` varchar(10) NOT NULL COMMENT ''业务类型'',
  `opt_department_id` bigint(20) NOT NULL COMMENT ''操作部门id'',
  `unit_code` varchar(50) NOT NULL COMMENT ''结算代码code'',
  `create_time` datetime DEFAULT NULL COMMENT ''创建时间'',
  `create_user` varchar(50) DEFAULT NULL COMMENT ''创建人'',
  `update_user` varchar(50) DEFAULT NULL COMMENT ''更新人'',
  `update_time` datetime DEFAULT NULL COMMENT ''更新时间'',
  `status` int(10) DEFAULT '1' COMMENT '状态（0禁用 1启用 2删除）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=''客户结算单位'';