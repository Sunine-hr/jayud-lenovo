CREATE TABLE `customs_declaration_filing` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `box_num` varchar(50) NOT NULL COMMENT '箱单号',
  `filing_date` varchar(255) NOT NULL COMMENT '归档日期',
  `biz_model` int(1) NOT NULL COMMENT '业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆)',
  `Im_and_ex_type` int(10) NOT NULL COMMENT '进出口类型(1进口 2出口)',
  `status` int(1) DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报关归档';


CREATE TABLE `customs_decl_filing_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customs_decl_filing_id` bigint(20) NOT NULL COMMENT '报关归档id',
  `yun_customs_no` varchar(50) NOT NULL COMMENT '云报关号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报关归档记录';