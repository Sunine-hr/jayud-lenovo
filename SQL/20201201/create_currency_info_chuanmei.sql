--20201127 财务功能-货币基表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for currency_info
-- ----------------------------
DROP TABLE IF EXISTS `currency_info`;
CREATE TABLE `currency_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增加id',
  `currency_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种代码',
  `currency_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态(0无效 1有效)',
  `create_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建用户名',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of currency_info
-- ----------------------------
INSERT INTO `currency_info` VALUES (1, 'CNY', '人民币', '1', 'admin', '2020-11-13 13:38:32');
INSERT INTO `currency_info` VALUES (2, 'HKD', '港元', '1', 'admin', '2020-11-13 13:38:35');
INSERT INTO `currency_info` VALUES (3, 'EUR', '欧元', '1', 'admin', '2020-11-13 13:38:37');
INSERT INTO `currency_info` VALUES (4, 'JPY', '日本日圆', '1', 'admin', '2020-11-13 13:39:27');
INSERT INTO `currency_info` VALUES (5, 'TWD', '新台币元', '1', 'admin', '2020-11-13 13:42:45');
INSERT INTO `currency_info` VALUES (6, 'GBP', '英镑', '1', 'admin', '2020-11-13 13:42:47');
INSERT INTO `currency_info` VALUES (7, 'USD', '美元', '1', 'admin', '2020-11-13 13:42:49');
INSERT INTO `currency_info` VALUES (8, 'MYR', '林吉特', '1', 'admin', '2020-11-13 13:42:50');
INSERT INTO `currency_info` VALUES (9, 'SGD', '新加坡元', '1', 'admin', '2020-11-13 13:42:52');

SET FOREIGN_KEY_CHECKS = 1;