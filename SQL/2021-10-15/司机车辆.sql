CREATE TABLE `oil_card_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oil_card_num` varchar(50) NOT NULL COMMENT '油卡卡号',
  `oil_name` varchar(50) NOT NULL COMMENT '油卡名称',
  `driver_id` bigint(20) DEFAULT NULL COMMENT '领用人id(司机id)',
  `vehicle_id` bigint(20) DEFAULT NULL COMMENT '当前使用车辆',
  `balance` decimal(20,2) NOT NULL DEFAULT '0.00' COMMENT '当前余额(元)',
  `oil_type` int(5) DEFAULT NULL COMMENT '油卡类型(1:充值卡,2:共享卡)',
  `oil_pwd` varchar(18) NOT NULL COMMENT '油卡密码',
  `oil_status` int(5) NOT NULL COMMENT '油卡状态(1:使用中,2:闲置中)',
  `recharge_type` int(5) DEFAULT NULL COMMENT '充值方式(1:微信,2:支付宝,3:现金)',
  `status` int(10) DEFAULT '1' COMMENT '状态（0:禁用 1:启用 2:删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `return_date` datetime DEFAULT NULL COMMENT '归还日期',
  `consuming_date` datetime DEFAULT NULL COMMENT '领用日期',
  `recharge_date` datetime DEFAULT NULL COMMENT '充值日期',
  `recharge_amount` decimal(20,2) DEFAULT NULL COMMENT '充值金额',
  `consumption_date` datetime DEFAULT NULL COMMENT '消费日期',
  `consumption_amount` decimal(20,2) DEFAULT NULL COMMENT '消费金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='油卡管理';


CREATE TABLE `oil_card_tracking_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(50) NOT NULL COMMENT '类型',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `oil_card_id` bigint(20) NOT NULL COMMENT '油卡管理id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='油卡跟踪信息';