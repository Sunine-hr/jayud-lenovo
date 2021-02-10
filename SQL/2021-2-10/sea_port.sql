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

 Date: 10/02/2021 15:52:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sea_port
-- ----------------------------
DROP TABLE IF EXISTS `sea_port`;
CREATE TABLE `sea_port`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船港口代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '船港口名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `status` int(10) NULL DEFAULT 1 COMMENT '状态(0无效 1有效)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '船港口地址表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
