
CREATE TABLE `warehouse_goods`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品对应的订单号',
  `order_id` bigint(10) NULL DEFAULT NULL COMMENT '商品对应的订单id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `sku` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sku',
  `specification_model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `board_number` int(10) NULL DEFAULT NULL COMMENT '板数',
  `commodity_batch_number` int(10) NULL DEFAULT NULL COMMENT '商品批次号',
  `number` int(10) NULL DEFAULT NULL COMMENT '件数',
  `pcs` int(10) NULL DEFAULT NULL COMMENT 'pcs',
  `weight` double(10, 0) NULL DEFAULT NULL COMMENT '重量',
  `volume` double(10, 0) NULL DEFAULT NULL COMMENT '体积',
  `estimated_arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '预计到达时间',
  `expected_delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '预计出库时间',
  `remarks` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件地址（多个以逗号隔开）',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称（多个以逗号隔开）',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NULL DEFAULT NULL COMMENT '商品类型 1为入库  2为出库',
  `warehousing_batch_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库批次号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 246 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓储商品信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
