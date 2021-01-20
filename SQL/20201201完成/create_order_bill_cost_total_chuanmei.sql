--20201127 财务功能-出账后根据结算币种转换后的金额信息表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_bill_cost_total
-- ----------------------------
DROP TABLE IF EXISTS `order_bill_cost_total`;
CREATE TABLE `order_bill_cost_total`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出账订单维度的费用统计ID',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `bill_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账单编号',
  `currency_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算币种',
  `cost_id` bigint(20) NULL DEFAULT NULL COMMENT '费用ID',
  `cost_info_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用项',
  `money` decimal(10, 4) NULL DEFAULT NULL COMMENT '金额',
  `local_money` decimal(10, 4) NULL DEFAULT NULL COMMENT '本币金额',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '1-有效 0-无效',
  `money_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '1-应付 2-应收',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出账订单维度的费用统计记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;