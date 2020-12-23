--20201031 基础信息
CREATE TABLE `vehicle_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plate_number` varchar(50) DEFAULT NULL COMMENT '大陆车牌',
  `hk_number` varchar(50) DEFAULT NULL COMMENT '香港车牌',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商id(supplier_info id)',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商名字(supplier_info name)',
  `pt_company` varchar(50) DEFAULT NULL COMMENT '牌头公司',
  `pt_fax` varchar(50) DEFAULT NULL COMMENT '牌头传真',
  `car_type` int(10) DEFAULT NULL COMMENT '车辆类型(1吨车 2柜车)',
  `customs_code` varchar(50) DEFAULT NULL COMMENT '海关编码',
  `card_number` varchar(50) DEFAULT NULL COMMENT '通关卡号',
  `weight` double(10,2) DEFAULT NULL COMMENT '吉车重量',
  `files` varchar(255) DEFAULT NULL COMMENT '附件,多个时用逗号分隔',
  `status` char(1) DEFAULT '1' COMMENT '启用状态0-禁用，1-启用',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商对应车辆信息';