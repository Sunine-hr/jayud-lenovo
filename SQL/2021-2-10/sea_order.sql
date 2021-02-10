/*
 Navicat Premium Data Transfer

 Source Server         : 113.100.140.250
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 113.100.140.250:6334
 Source Schema         : jayud_oms

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 10/02/2021 15:52:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sea_order
-- ----------------------------
DROP TABLE IF EXISTS `sea_order`;
CREATE TABLE `sea_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '海运订单主键',
  `main_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单编号',
  `order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海运订单编号',
  `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(S_0待接单,S_1海运接单,S_2订船,S_3订单入仓,\r\nS_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收)',
  `process_status` int(10) NULL DEFAULT NULL COMMENT '流程状态(0:进行中,1:完成,2:草稿,3.关闭)',
  `unit_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位code',
  `legal_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单法人名称',
  `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id',
  `imp_and_exp_type` int(10) NULL DEFAULT NULL COMMENT '进出口类型(1：进口，2：出口)',
  `terms` int(10) NULL DEFAULT NULL COMMENT '贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)',
  `port_departure_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运港代码',
  `port_destination_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的港代码',
  `overseas_suppliers_id` bigint(20) NULL DEFAULT NULL COMMENT '海外供应商id',
  `proxy_service_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代理服务类型（0:清关,1:配送）多个逗号隔开',
  `good_time` datetime(0) NULL DEFAULT NULL COMMENT '货好时间',
  `is_freight_collect` tinyint(1) NULL DEFAULT NULL COMMENT '运费是否到付(1代表true,0代表false)',
  `is_other_expenses_paid` tinyint(1) NULL DEFAULT NULL COMMENT '其他费用是否到付(1代表true,0代表false)',
  `is_dangerous_goods` tinyint(1) NULL DEFAULT NULL COMMENT '是否危险品(1代表true,0代表false)',
  `is_charged` tinyint(1) NULL DEFAULT NULL COMMENT '是否带电(1代表true,0代表false)',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `need_input_cost` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否需要录入费用(0:false,1:true)',
  `order_taker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单人(登录用户名)',
  `receiving_orders_date` datetime(0) NULL DEFAULT NULL COMMENT '接单日期',
  `unit_department_id` bigint(20) NULL DEFAULT NULL COMMENT '结算部门',
  `cut_replenish_time` datetime(0) NULL DEFAULT NULL COMMENT '截补料时间',
  `cabinet_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号',
  `paper_strip_seal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条',
  `cabinet_size` int(30) NULL DEFAULT NULL COMMENT '柜型大小',
  `cabinet_type` int(50) NULL DEFAULT NULL COMMENT '柜型类型',
  `cabinet_size_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型大小名字',
  `cabinet_type_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型类型名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '海运订单表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
