CREATE TABLE `contract_quotation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) NOT NULL COMMENT '报价编号',
  `customer_code` varchar(20) NOT NULL COMMENT '客户code',
  `contract_no` varchar(500) DEFAULT NULL COMMENT '合同编号',
  `name` varchar(50) DEFAULT NULL COMMENT '报价名称',
  `start_time` date DEFAULT NULL COMMENT '有效起始时间',
  `end_time` date DEFAULT NULL COMMENT '有效结束时间',
  `audit_status` int(5) DEFAULT '2' COMMENT '审核状态(1:已审核,2:未审核)',
  `status` int(5) DEFAULT '1' COMMENT '状态（0禁用 1启用 2删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='合同报价';

CREATE TABLE `contract_quotation_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sub_type` varchar(20) NOT NULL COMMENT '子订单类型',
  `contract_quotation_id` bigint(20) DEFAULT NULL COMMENT '合同报价id',
  `type` int(10) DEFAULT NULL COMMENT '类型(1:整车 2:其他)',
  `starting_place` varchar(255) DEFAULT NULL COMMENT '起始地',
  `destination` varchar(255) DEFAULT NULL COMMENT '目的地',
  `vehicle_size` varchar(10) DEFAULT NULL COMMENT '车型(3T 5T 8T 10T 12T 20GP 40GP 45GP)',
  `cost_code` varchar(20) DEFAULT NULL COMMENT '费用名称code',
  `unit_price` decimal(10,4) DEFAULT NULL COMMENT '单价',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `currency_code` varchar(50) DEFAULT NULL COMMENT '币种代码',
  `cost_type_id` bigint(20) DEFAULT NULL COMMENT '费用类别(作业环节)',
  `status` int(10) DEFAULT '1' COMMENT '状态（0禁用 1启用 2删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `starting_place_id` varchar(500) DEFAULT NULL COMMENT '起始地id(多个逗号隔开)',
  `destination_id` varchar(500) DEFAULT NULL COMMENT '目的地id(多个逗号隔开)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='合同报价详情';


CREATE TABLE `tracking_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) NOT NULL COMMENT '类型',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `business_id` bigint(20) NOT NULL COMMENT '业务id',
  `business_type` int(10) NOT NULL COMMENT '业务类型(1:合同报价)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `opt_type` int(5) DEFAULT NULL COMMENT '操作类型(1:修改,2:删除,3:新增)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='油卡跟踪信息';