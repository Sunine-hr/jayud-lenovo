

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `trailer_dispatch`;
CREATE TABLE `trailer_dispatch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '拖车订单id',
  `trailer_order_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拖车订单号',
  `order_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '派车单号',
  `plate_number` bigint(50) NULL DEFAULT NULL COMMENT '大陆车牌',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机电话',
  `supplier_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名字(supplier_info name)',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `status` int(10) NULL DEFAULT NULL COMMENT '状态（0确认，1待确认，2删除）',
  `weighing` double(20, 2) NULL DEFAULT NULL COMMENT '过磅重量',
  `name` bigint(50) NULL DEFAULT NULL COMMENT '司机姓名',
  `review_comments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拖车派车信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
