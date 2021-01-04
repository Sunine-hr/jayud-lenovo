CREATE TABLE `service_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(50) NOT NULL COMMENT '服务类型(0:费用补录,1:单证费用,2:存仓费用,快递费用)',
  `associated_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '关联订单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务单';