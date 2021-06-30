

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sea_bill
-- ----------------------------
DROP TABLE IF EXISTS `sea_bill`;
CREATE TABLE `sea_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单订单号',
  `sea_order_id` bigint(20) NULL DEFAULT NULL COMMENT '海运订单id',
  `sea_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海运订单号',
  `cut_replenish_time` datetime(0) NULL DEFAULT NULL COMMENT '截补料时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `imp_and_exp_type` int(11) NULL DEFAULT NULL COMMENT '进出口类型(1：进口，2：出口)',
  `terms` int(11) NULL DEFAULT NULL COMMENT '贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)',
  `port_departure_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运港代码',
  `port_destination_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的港代码',
  `good_time` datetime(0) NULL DEFAULT NULL COMMENT '货好时间',
  `is_freight_collect` tinyint(1) NULL DEFAULT NULL COMMENT '运费是否到付(1代表true,0代表false)',
  `is_other_expenses_paid` tinyint(1) NULL DEFAULT NULL COMMENT '其他费用是否到付(1代表true,0代表false)',
  `is_dangerous_goods` tinyint(1) NULL DEFAULT NULL COMMENT '是否危险品(1代表true,0代表false)',
  `is_charged` tinyint(1) NULL DEFAULT NULL COMMENT '是否带电(1代表true,0代表false)',
  `transit_port_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中转港',
  `cabinet_type` int(11) NULL DEFAULT NULL COMMENT '柜型类型',
  `cabinet_type_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型类型名字',
  `transport_clause` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输方式',
  `ship_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船名字',
  `ship_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船次',
  `delivery_mode` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出单方式',
  `additional_service` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附加服务',
  `destination` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地',
  `place_of_delivery` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地',
  `closing_time` datetime(0) NULL DEFAULT NULL COMMENT '截关时间',
  `cut_off_time` datetime(0) NULL DEFAULT NULL COMMENT '截仓时间',
  `so` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SO',
  `sailing_time` datetime(0) NULL DEFAULT NULL COMMENT '开船时间',
  `ordering_information` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订柜信息',
  `shipper_information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发货人信息',
  `consignee_information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人信息',
  `notifier_information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知人信息',
  `agent_information` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理人信息',
  `shipping_mark` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唛头',
  `good_name` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货物名称',
  `board_number` int(15) NULL DEFAULT NULL COMMENT '板数',
  `plate_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '板数单位',
  `number` int(15) NULL DEFAULT NULL COMMENT '件数',
  `number_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '件数单位',
  `weight` double(20, 4) NULL DEFAULT NULL COMMENT '总重量',
  `volume` double(20, 4) NULL DEFAULT NULL COMMENT '体积',
  `bill_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单号',
  `delivery_wharf` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交仓码头',
  `voyage` int(10) NULL DEFAULT NULL COMMENT '航程',
  `type` int(1) NULL DEFAULT NULL COMMENT '提单类型（1为提单，2为拼柜提单）',
  `is_spell` tinyint(1) NULL DEFAULT NULL COMMENT '是否拼柜(1代表true,0代表false)',
  `spell_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼柜的提单号集合',
  `number_of_bl` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单份数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提单信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



ALTER TABLE `sea_order`
ADD COLUMN `destination` varchar(50) NULL COMMENT '目的地' AFTER `audit_status`;

ALTER TABLE `sea_order`
ADD COLUMN `place_of_delivery` varchar(50) NULL COMMENT '收货地' AFTER `destination`;

ALTER TABLE `sea_order`
ADD COLUMN `department_id` bigint(10) NULL COMMENT '收货地' AFTER `place_of_delivery`;

ALTER TABLE `sea_order`
ADD COLUMN `closing_time` datetime NULL COMMENT '截关时间' AFTER `department_id`;

ALTER TABLE `sea_order`
ADD COLUMN `cut_off_time` datetime NULL COMMENT '截仓时间' AFTER `closing_time`;
ALTER TABLE `sea_order`
ADD COLUMN `so` varchar(30) NULL COMMENT 'so' AFTER `cut_off_time`;
ALTER TABLE `sea_order`
ADD COLUMN `file_path` varchar(200) NULL COMMENT '附件地址' AFTER `so`;
ALTER TABLE `sea_order`
ADD COLUMN `file_name` varchar(200) NULL COMMENT '附件名字' AFTER `file_path`;

ALTER TABLE `sea_replenishment`
ADD COLUMN `destination` varchar(50) NULL COMMENT '目的地' AFTER `is_release_order`;

ALTER TABLE `sea_replenishment`
ADD COLUMN `place_of_delivery` varchar(50) NULL COMMENT '收货地' AFTER `destination`;

ALTER TABLE `sea_replenishment`
ADD COLUMN `closing_time` datetime NULL COMMENT '截关时间' AFTER `place_of_delivery`;

ALTER TABLE `sea_replenishment`
ADD COLUMN `cut_off_time` datetime NULL COMMENT '截仓时间' AFTER `closing_time`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `so` varchar(30) NULL COMMENT 'so' AFTER `cut_off_time`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `transport_clause` varchar(50) NULL COMMENT '运输条款' AFTER `so`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `ship_name` varchar(50) NULL COMMENT '船名' AFTER `transport_clause`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `ship_number` varchar(50) NULL COMMENT '船次' AFTER `ship_name`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `delivery_mode` varchar(10) NULL COMMENT '出单方式' AFTER `ship_number`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `additional_service` varchar(200) NULL COMMENT '附加服务' AFTER `delivery_mode`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `sailing_time` datetime NULL COMMENT '开船时间' AFTER `additional_service`;
ALTER TABLE `sea_replenishment`
ADD COLUMN `ordering_information` varchar(50) NULL COMMENT '订柜信息' AFTER `sailing_time`;


ALTER TABLE `goods`
MODIFY COLUMN `name` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ;
ALTER TABLE `goods`
MODIFY COLUMN `label` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ;
ALTER TABLE `order_address`
MODIFY COLUMN `address` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ;