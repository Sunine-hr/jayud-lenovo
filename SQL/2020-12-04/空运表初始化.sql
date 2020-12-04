
CREATE TABLE `air_booking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态(0:确认,1:待确认)',
  `air_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '空运订单编号',
  `air_order_id` bigint(20) DEFAULT NULL COMMENT '空运订单id',
  `agent_supplier_id` bigint(20) NOT NULL COMMENT '代理供应商id',
  `warehousing_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入仓号',
  `main_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '主单号',
  `sub_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分单号',
  `cut_off_date` datetime DEFAULT NULL COMMENT '截关日期',
  `airline_company` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '航空公司',
  `flight` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '航班',
  `etd` datetime DEFAULT NULL COMMENT '预计离港时间',
  `atd` datetime DEFAULT NULL COMMENT '实际离岗时间',
  `eta` datetime DEFAULT NULL COMMENT '预计到港时间',
  `ata` datetime DEFAULT NULL COMMENT '实际到港时间',
  `delivery_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交仓地址',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件路径(多个逗号隔开)',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件名称(多个逗号隔开)',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='空运订舱表';


CREATE TABLE `air_extension_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint(20) NOT NULL COMMENT '业务主键',
  `business_table` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务表(例如:air_order)',
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '数据(json格式)',
  `type` int(10) NOT NULL COMMENT '类型(0:本系统,1:vivo,待定)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述(也可以当key值使用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='空运扩展字段表';


CREATE TABLE `air_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '空运订单主键',
  `main_order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '主订单编号',
  `order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '空运订单编号',
  `third_party_order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '第三方订单编号',
  `status` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓,\r\nk_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)',
  `process_status` int(10) DEFAULT NULL COMMENT '流程状态(0:进行中,1:完成)',
  `settlement_unit_code` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '结算单位code',
  `legal_id` bigint(20) DEFAULT NULL COMMENT '接单法人',
  `imp_and_exp_type` int(10) DEFAULT NULL COMMENT '进出口类型(1：进口，2：出口)',
  `terms` int(10) DEFAULT NULL COMMENT '贸易方式(0:CIF,1:DUU,2:FOB,3:DDP)',
  `port_departure_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '起运港代码',
  `port_destination_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '目的港代码',
  `overseas_suppliers_id` bigint(20) DEFAULT NULL COMMENT '海外供应商id',
  `proxy_service_type` varchar(20) DEFAULT NULL COMMENT '代理服务类型（0:清关,1:配送）多个逗号隔开',
  `good_time` datetime DEFAULT NULL COMMENT '货好时间',
  `is_freight_collect` tinyint(1) DEFAULT NULL COMMENT '运费是否到付(1代表true,0代表false)',
  `is_other_expenses_paid` tinyint(1) DEFAULT NULL COMMENT '其他费用是否到付(1代表true,0代表false)',
  `is_dangerous_goods` tinyint(1) DEFAULT NULL COMMENT '是否危险品(1代表true,0代表false)',
  `is_charged` tinyint(1) DEFAULT NULL COMMENT '是否带电(1代表true,0代表false)',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user_type` int(5) DEFAULT NULL COMMENT '创建人的类型(0:本系统,1:vivo)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='空运订单表';


CREATE TABLE `air_port` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '飞机港口代码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '飞机港口名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='飞机港口地址表';

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint(20) NOT NULL COMMENT '业务主键(根据类型选择对应表的主键)',
  `business_type` int(10) NOT NULL COMMENT '业务类型(0:空运)',
  `label` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签（空运是唛头）',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '货品名称',
  `plate_amount` int(10) DEFAULT NULL COMMENT '板数',
  `plate_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '板数单位',
  `bulk_cargo_amount` int(10) DEFAULT NULL COMMENT '散货件数',
  `bulk_cargo_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '散货单位',
  `size` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '尺寸(长宽高)',
  `total_weight` double(10,2) DEFAULT NULL COMMENT '总重量',
  `volume` double(10,2) DEFAULT NULL COMMENT '体积',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='货品信息表';

CREATE TABLE `order_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint(20) NOT NULL COMMENT '业务主键(根据类型选择对应表的主键)',
  `type` int(10) NOT NULL COMMENT '类型(0:发货,1:收货,2:通知)',
  `business_type` int(10) DEFAULT NULL COMMENT '业务类型(0:空运)',
  `company` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公司名称',
  `contacts` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人',
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '详细地址',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `fax` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '传真',
  `mailbox` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `remarks` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单地址表';