CREATE TABLE `order_inland_transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内陆订单主键',
  `main_order_no` varchar(200) DEFAULT NULL COMMENT '主订单编号',
  `order_no` varchar(200) DEFAULT NULL COMMENT '内陆订单编号',
  `process_status` int(10) DEFAULT NULL COMMENT '流程状态(0:进行中,1:完成,2:草稿,3.关闭)',
  `status` char(10) DEFAULT NULL COMMENT '状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核,\r\nNL_3_1派车审核不通过,NL_3_2派车审核驳回,\r\nNL_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)',
  `vehicle_type` int(10) DEFAULT NULL COMMENT '车型(1吨车 2柜车)',
  `vehicle_size` varchar(5) DEFAULT NULL COMMENT '车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)',
  `unit_code` varchar(50) DEFAULT NULL COMMENT '结算单位CODE',
  `unit_name` varchar(255) DEFAULT NULL COMMENT '结算单位名称',
  `legal_name` varchar(50) DEFAULT NULL COMMENT '接单法人',
  `legal_entity_id` bigint(20) DEFAULT NULL COMMENT '法人主体ID',
  `need_input_cost` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否需要录入费用',
  `order_taker` varchar(50) DEFAULT NULL COMMENT '接单人(登录用户名)',
  `receiving_orders_date` datetime DEFAULT NULL COMMENT '接单日期',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='内陆订单';



CREATE TABLE `order_inland_send_cars` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `transport_no` varchar(100) NOT NULL COMMENT '运输订单号',
  `order_no` varchar(255) NOT NULL COMMENT '子订单编号',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `vehicle_type` int(10) NOT NULL COMMENT '车型(1吨车 2柜车)',
  `vehicle_size` varchar(5) NOT NULL COMMENT '车型(例如:3T)',
  `driver_name` varchar(20) NOT NULL COMMENT '司机名称',
  `driver_phone` varchar(50) NOT NULL COMMENT '司机电话',
  `license_plate` varchar(20) NOT NULL COMMENT '车牌号',
  `vehicle_id` bigint(20) DEFAULT NULL COMMENT '车辆id',
  `supplier_name` varchar(255) NOT NULL COMMENT '供应商名称',
  `supplier_id` bigint(100) NOT NULL COMMENT '供应商id',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `describes` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='内陆派车信息';