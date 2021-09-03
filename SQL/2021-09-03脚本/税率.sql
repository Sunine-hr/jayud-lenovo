CREATE TABLE `cost_genre_tax_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cost_genre_id` bigint(20) NOT NULL COMMENT '费用类型id',
  `cost_type_id` bigint(20) DEFAULT NULL COMMENT '费用类别id',
  `tax_rate` decimal(20,2) DEFAULT NULL COMMENT '税率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='费用类型税率表';