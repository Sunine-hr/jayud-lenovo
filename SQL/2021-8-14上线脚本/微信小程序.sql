--  司机增加微信小程序id
ALTER TABLE `driver_info`
ADD COLUMN `applet_id` varchar(50) NULL COMMENT '微信小程序id' AFTER `password`;

-- 中港地址增加经纬度
ALTER TABLE `delivery_address`
ADD COLUMN `lo_and_la` varchar(255) NULL COMMENT '腾讯经纬度' AFTER `mailbox`;


ALTER TABLE `order_payment_cost`
ADD COLUMN `driver_cost_id` bigint(20) NULL COMMENT '司机费用id' AFTER `unit_name`;


CREATE TABLE `applet_order_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL COMMENT '中港订单id',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `port_name` varchar(50) DEFAULT NULL COMMENT '口岸名称',
  `record_status` int(10) DEFAULT NULL COMMENT '状态(1:取消)',
  `pick_up_province` varchar(50) DEFAULT NULL COMMENT '省（提货）',
  `pick_up_city` varchar(50) DEFAULT NULL COMMENT '市（提货）',
  `pick_up_area` varchar(50) DEFAULT NULL COMMENT '区(提货)',
  `pick_la_and_lo` varchar(255) DEFAULT NULL COMMENT '提货经纬度',
  `receiving_province` varchar(50) DEFAULT NULL COMMENT '省（送货）',
  `receiving_city` varchar(50) DEFAULT NULL COMMENT '市（送货）',
  `receiving_area` varchar(50) DEFAULT NULL COMMENT '区(送货)',
  `receiving_la_and_lo` varchar(50) DEFAULT NULL COMMENT '送货经纬度',
  `goods_desc` varchar(50) DEFAULT NULL COMMENT '货物信息',
  `time` varchar(50) DEFAULT NULL COMMENT '中港订单时间',
  `address` varchar(255) DEFAULT NULL COMMENT '送货详细地址(中转仓库)',
  `contacts` varchar(255) DEFAULT NULL COMMENT '联系人(中转仓库)',
  `contact_number` varchar(255) DEFAULT NULL COMMENT '联系电话(中转仓库)',
  `is_virtual` varchar(255) DEFAULT NULL COMMENT '是否虚拟仓',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` varchar(255) DEFAULT NULL COMMENT '订单状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序订单记录';

