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

 Date: 18/02/2021 18:42:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cabinet_type
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_type`;
CREATE TABLE `cabinet_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '柜型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cabinet_type
-- ----------------------------
INSERT INTO `cabinet_type` VALUES (1, 'FCL', '2021-02-05 14:32:09', 'admin', '1');
INSERT INTO `cabinet_type` VALUES (2, 'LCL', '2021-02-05 14:33:25', 'admin', '1');

SET FOREIGN_KEY_CHECKS = 1;
