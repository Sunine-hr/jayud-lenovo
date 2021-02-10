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

 Date: 10/02/2021 15:52:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sea_bookship
-- ----------------------------
DROP TABLE IF EXISTS `sea_bookship`;
CREATE TABLE `sea_bookship`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status` int(5) NULL DEFAULT NULL COMMENT '状态(0:确认,1:待确认,2:删除)',
  `sea_order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '海运订单编号',
  `sea_order_id` bigint(20) NULL DEFAULT NULL COMMENT '海运订单id',
  `agent_supplier_id` bigint(20) NOT NULL COMMENT '代理供应商id',
  `warehousing_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入仓号',
  `main_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主单号',
  `sub_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分单号',
  `ship_company` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船公司',
  `ship_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船名字',
  `ship_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船次',
  `etd` datetime(0) NULL DEFAULT NULL COMMENT '预计离港时间',
  `atd` datetime(0) NULL DEFAULT NULL COMMENT '实际离岗时间',
  `eta` datetime(0) NULL DEFAULT NULL COMMENT '预计到港时间',
  `ata` datetime(0) NULL DEFAULT NULL COMMENT '实际到港时间',
  `delivery_wharf` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '交仓码头',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单文件路径(多个逗号隔开)',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单文件名称(多个逗号隔开)',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `bill_lading_weight` double NULL DEFAULT NULL COMMENT '提单重量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '海运订船表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
