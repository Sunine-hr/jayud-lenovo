--20201031 基础信息
CREATE TABLE `driver_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增加id',
  `name` varchar(100) DEFAULT NULL COMMENT '司机姓名',
  `is_main` char(1) DEFAULT NULL COMMENT '是否主司机(0否 1是)',
  `hk_phone` varchar(50) DEFAULT NULL COMMENT '香港电话',
  `phone` varchar(50) DEFAULT NULL COMMENT '大陆电话',
  `vehicle_id` bigint(10) DEFAULT NULL COMMENT '车牌号(vehicle_info id)',
  `id_no` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `driving_no` varchar(50) DEFAULT NULL COMMENT '驾驶证',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商id(supplier_info id)',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商名字(supplier_info name)',
  `status` char(1) DEFAULT NULL COMMENT '启用状态0-禁用，1-启用',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `up_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商对应司机信息';