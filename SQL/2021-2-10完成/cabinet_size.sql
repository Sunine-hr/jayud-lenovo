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

 Date: 19/02/2021 17:02:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cabinet_size
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_size`;
CREATE TABLE `cabinet_size`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型大小名',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '柜型大小表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cabinet_size
-- ----------------------------
INSERT INTO `cabinet_size` VALUES (1, '20GP', 'admin', '1');
INSERT INTO `cabinet_size` VALUES (2, '40HQ', 'admin', '1');
INSERT INTO `cabinet_size` VALUES (3, '45GP', 'admin', '1');
INSERT INTO `cabinet_size` VALUES (4, '40GP', 'admin', '1');
INSERT INTO `cabinet_size` VALUES (5, '45HQ', 'admin', '1');

SET FOREIGN_KEY_CHECKS = 1;
