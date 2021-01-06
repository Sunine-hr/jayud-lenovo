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

 Date: 06/01/2021 15:56:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for vehicle_size_info
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_size_info`;
CREATE TABLE `vehicle_size_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '车型尺寸ID',
  `vehicle_size` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '尺寸',
  `vehicle_type` int(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车型(2-柜车 1-吨车)',
  `created_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vehicle_size_info
-- ----------------------------
INSERT INTO `vehicle_size_info` VALUES (1, '3T', 1, 'admin', '2021-01-06 15:51:20');
INSERT INTO `vehicle_size_info` VALUES (2, '5T', 1, 'admin', '2021-01-06 15:52:53');
INSERT INTO `vehicle_size_info` VALUES (3, '8T', 1, 'admin', '2021-01-06 15:52:55');
INSERT INTO `vehicle_size_info` VALUES (4, '10T', 1, 'admin', '2021-01-06 15:54:31');
INSERT INTO `vehicle_size_info` VALUES (5, '12T', 1, 'admin', '2021-01-06 15:54:33');
INSERT INTO `vehicle_size_info` VALUES (6, '15T', 1, 'admin', '2021-01-06 15:54:48');
INSERT INTO `vehicle_size_info` VALUES (7, '20GP', 2, 'admin', '2021-01-06 15:55:08');
INSERT INTO `vehicle_size_info` VALUES (8, '40GP', 2, 'admin', '2021-01-06 15:55:21');
INSERT INTO `vehicle_size_info` VALUES (9, '45GP', 2, 'admin', '2021-01-06 15:55:33');

SET FOREIGN_KEY_CHECKS = 1;
