
CREATE TABLE `storage_input_order`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库订单号',
  `main_order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单号',
  `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态()',
  `process_status` int(10) NULL DEFAULT NULL COMMENT '流程状态(0:进行中,1:完成,2:草稿,3.关闭)',
  `unit_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位code',
  `legal_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单法人名称',
  `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '操作部门id',
  `waybill_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运单号',
  `plate_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `warehouse_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入仓号',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `order_taker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单人(登录用户名)',
  `receiving_orders_date` datetime(0) NULL DEFAULT NULL COMMENT '接单日期',
  `is_submit` int(1) NULL DEFAULT NULL COMMENT '确认入仓是否提交（0未提交，1已提交）',
  `storage_time` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库订单表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
