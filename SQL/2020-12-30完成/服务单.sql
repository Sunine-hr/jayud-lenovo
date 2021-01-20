DROP TABLE IF EXISTS `service_order`;
CREATE TABLE `service_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号(生成规则product_classify code+随时数)',
  `main_order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主订单号',
  `type` int(50) NOT NULL COMMENT '服务类型(service_type)',
  `associated_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联订单',
  `created_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `created_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updated_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `updated_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务单' ROW_FORMAT = Dynamic;