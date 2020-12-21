--20201127 财务功能-应付录入费用表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_payment_cost
-- ----------------------------
DROP TABLE IF EXISTS `order_payment_cost`;
CREATE TABLE `order_payment_cost`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `main_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单号',
  `biz_order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务订单号',
  `order_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际产生业务订单号',
  `biz_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务类型(product_biz code)',
  `biz_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名(product_biz name)',
  `customer_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '由客户变为供应商CODE',
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '由客户变为供应商名称',
  `cost_type_id` bigint(20) NULL DEFAULT NULL COMMENT '费用类别',
  `cost_genre_id` bigint(20) NULL DEFAULT NULL COMMENT '费用类型',
  `cost_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收费项目code',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `unit_price` decimal(10, 4) NULL DEFAULT NULL COMMENT '单价',
  `number` int(10) NULL DEFAULT NULL COMMENT '数量',
  `currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种代码',
  `amount` decimal(10, 4) NULL DEFAULT NULL COMMENT '应收金额',
  `exchange_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '汇率',
  `change_amount` decimal(10, 4) NULL DEFAULT NULL COMMENT '本币金额',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_bill` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '1-暂存 0-未出账 2-已出账 save_confirm-添加确定',
  `sub_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '如果是已子订单维度出账的,则记录具体的子订单类型',
  `is_sum_to_main` tinyint(1) NULL DEFAULT NULL COMMENT '是否汇总到主订单',
  `status` int(10) NULL DEFAULT NULL COMMENT '审核状态，COST_0(\"0\", \"审核驳回\"),\r\n    COST_1(\"1\", \"草稿\"),\r\n    COST_2(\"2\", \"提交审核\"),\r\n    COST_3(\"3\", \"审核通过\");',
  `opt_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `opt_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `created_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `created_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多个文件，用逗号隔开',
  `files` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多个文件路径,用逗号隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单对应应付费用明细' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;