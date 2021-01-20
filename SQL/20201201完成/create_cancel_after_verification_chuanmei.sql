--20201127 财务功能-核销表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cancel_after_verification
-- ----------------------------
DROP TABLE IF EXISTS `cancel_after_verification`;
CREATE TABLE `cancel_after_verification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '核销ID',
  `bill_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单编号',
  `real_receive_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '实收金额',
  `currency_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种',
  `exchange_rate` decimal(10, 5) NULL DEFAULT NULL COMMENT '汇率',
  `discount_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '折合金额',
  `opr_mode` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核销方式',
  `real_receive_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '实际收款时间',
  `local_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '本币金额',
  `created_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '核销表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;