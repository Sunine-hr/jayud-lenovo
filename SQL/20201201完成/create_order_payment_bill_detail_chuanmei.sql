--20201127 财务功能-应付对账明细表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_payment_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_payment_bill_detail`;
CREATE TABLE `order_payment_bill_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '应付对账单详情ID',
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT '应付对账单ID',
  `account_term` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '核算期 XXXX-XX',
  `settlement_currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算币种',
  `bill_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账单编号',
  `make_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成账单人',
  `make_time` timestamp(0) NULL DEFAULT NULL COMMENT '生成账单时间',
  `audit_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `apply_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '付款申请 0-未申请  1-待审核 2-审核通过 3-审核驳回',
  `payment_amount` decimal(10, 4) NULL DEFAULT NULL COMMENT '付款金额',
  `audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核状态',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '1-有效 0-无效',
  `push_kingdee_count` int(5) NOT NULL DEFAULT 0 COMMENT '推送金蝶次数',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `created_order_time` date NOT NULL COMMENT '建单日期',
  `start_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运地',
  `end_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地',
  `license_plate` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '大陆车牌号，HK车牌号',
  `vehicle_size` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车型 如：3T',
  `piece_num` int(5) NULL DEFAULT NULL COMMENT '订单维度的件数',
  `weight` double NULL DEFAULT NULL COMMENT '订单维度的重量',
  `yun_customs_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报关单号,多个逗号分隔',
  `cost_id` bigint(20) NOT NULL COMMENT '应收费用ID',
  `local_amount` decimal(10, 4) NOT NULL COMMENT '费用类型/类别/名称维度的本币金额',
  `tax_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '费用维度的税率',
  `remarks` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用维度的备注',
  `created_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `updated_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updated_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`, `order_no`, `cost_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应付出账记录详情表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;