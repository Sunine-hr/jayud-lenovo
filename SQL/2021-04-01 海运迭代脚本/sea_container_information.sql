SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sea_container_information`;
CREATE TABLE `sea_container_information`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sea_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海运订单号',
  `sea_rep_id` bigint(20) NULL DEFAULT NULL COMMENT '截补料id',
  `sea_rep_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '截补料单号',
  `cabinet_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型尺寸',
  `cabinet_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号',
  `paper_strip_seal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条',
  `weight` double(20, 2) NULL DEFAULT NULL COMMENT '重量',
  `volume` double(20, 2) NULL DEFAULT NULL COMMENT '体积',
  `plat_number` int(20) NULL DEFAULT NULL COMMENT '件数',
  `packing` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包装',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '海运货柜信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
