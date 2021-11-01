/*
 Navicat Premium Data Transfer

 Source Server         : 113.100.140.250-6306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 113.100.140.250:6306
 Source Schema         : jayud_platform

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 01/11/2021 10:15:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for camera
-- ----------------------------
DROP TABLE IF EXISTS `camera`;
CREATE TABLE `camera`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `flv` int(0) NULL DEFAULT NULL,
  `hls` int(0) NULL DEFAULT NULL,
  `ffmpeg` int(0) NULL DEFAULT NULL,
  `auto_close` int(0) NULL DEFAULT NULL,
  `type` int(0) NULL DEFAULT 0,
  `media_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '摄像头监视器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of camera
-- ----------------------------
INSERT INTO `camera` VALUES (4, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/101?transportmode=unicast', '货台1', 0, 0, 0, 0, 0, '386ce3049f161afcd446eb1852ea6dfe');
INSERT INTO `camera` VALUES (5, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/201?transportmode=unicast', '4楼北2', 0, 0, 0, 0, 0, '715a09d9350600fd8533b9e4c4de9c26');
INSERT INTO `camera` VALUES (6, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/301?transportmode=unicast', '4楼南1', 0, 0, 0, 0, 0, '671078ec89f5a206ade14e3a475d5baf');
INSERT INTO `camera` VALUES (7, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/401?transportmode=unicast', '4楼北1', 0, 0, 0, 0, 0, 'f922d352c7bec9ed1fca64fc75c9691a');
INSERT INTO `camera` VALUES (8, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/501?transportmode=unicast', '4楼北3', 0, 0, 0, 0, 0, '198bb15f478446bff10d112697ed51f2');
INSERT INTO `camera` VALUES (9, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/601?transportmode=unicast', '货台3', 0, 0, 0, 0, 0, '8b95ca73fb16af0a209c396e07318e43');
INSERT INTO `camera` VALUES (10, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/701?transportmode=unicast', '货台2', 0, 0, 0, 0, 0, 'b56a60d27431562623b45368d9483907');
INSERT INTO `camera` VALUES (11, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/901?transportmode=unicast', '一楼B货架通道', 0, 0, 0, 0, 0, 'd2b3679bec863867f86544543f0b124a');
INSERT INTO `camera` VALUES (12, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/1001?transportmode=unicast', '二楼E-F货架通道', 0, 0, 0, 0, 0, 'eef746d87c57f384a2dda786c672eae3');
INSERT INTO `camera` VALUES (13, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/1201?transportmode=unicast', '货台4', 0, 0, 0, 0, 0, '819d230aa6d2f32489b91457a5b78a90');
INSERT INTO `camera` VALUES (14, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/1301?transportmode=unicast', '一楼备货区', 0, 0, 0, 0, 0, 'ba2360626d6c7957581f35d8b0722abb');
INSERT INTO `camera` VALUES (15, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/1401?transportmode=unicast', '出货门口', 0, 0, 0, 0, 0, '20f308b8a6a6cd10de077c71e0c7cf9d');
INSERT INTO `camera` VALUES (16, 'rtsp://jyd:jyd123456@172b4696z9.imwork.net:554/Streaming/Channels/1601?transportmode=unicast', '一楼A货架', 0, 0, 0, 0, 0, '9cbe9ddbf207ee1fc90ea1f4a3ad2438');

SET FOREIGN_KEY_CHECKS = 1;

