--20201127 财务功能-货币汇率表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for currency_rate
-- ----------------------------
DROP TABLE IF EXISTS `currency_rate`;
CREATE TABLE `currency_rate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `dcid` int(10) NULL DEFAULT NULL COMMENT '兑换币种',
  `ocid` int(10) NULL DEFAULT NULL COMMENT '原始币种',
  `exchange_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '汇率',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)',
  `month` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '年月',
  `created_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种汇率' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;