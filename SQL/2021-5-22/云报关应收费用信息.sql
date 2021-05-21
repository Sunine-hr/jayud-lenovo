DROP TABLE IF EXISTS `yunbaoguan_receivable_cost`;
CREATE TABLE `yunbaoguan_receivable_cost` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_no` varchar(50) DEFAULT NULL COMMENT '报关单号(18位)',
  `uid` varchar(50) DEFAULT NULL COMMENT '委托单号',
  `receivable_cost_data` varchar(5000) DEFAULT NULL COMMENT '应收费用数据',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='云报关应收费用信息';
