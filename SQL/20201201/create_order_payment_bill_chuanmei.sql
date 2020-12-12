--20201127 财务功能-应付对账表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_payment_bill
-- ----------------------------
DROP TABLE IF EXISTS `order_payment_bill`;
CREATE TABLE `order_payment_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应付出账单ID',
  `legal_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '法人主体',
  `supplier_ch_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商,录入费用填的',
  `already_paid_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '已出账金额(人民币)',
  `bill_order_num` int(5) NULL DEFAULT NULL COMMENT '已出账订单数',
  `bill_num` int(5) NULL DEFAULT NULL COMMENT '账单数',
  `sub_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '如果是已子订单维度出账的,则记录具体的子订单类型\r\nmain or zgys or bg',
  `is_main` tinyint(1) NULL DEFAULT NULL COMMENT '是否以主订单维度出账的 1-是 0-否',
  `created_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updated_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应付出账记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;