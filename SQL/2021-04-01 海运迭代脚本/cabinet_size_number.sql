SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `cabinet_size_number`;
CREATE TABLE `cabinet_size_number`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sea_order_id` bigint(20) NULL DEFAULT NULL COMMENT '海运订单id',
  `sea_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海运订单编号',
  `cabinet_type_size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型',
  `number` int(10) NULL DEFAULT NULL COMMENT '柜量',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '柜型数量表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
