SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sea_replenishment`;
CREATE TABLE `sea_replenishment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '截补料单号',
  `sea_order_id` bigint(20) NULL DEFAULT NULL COMMENT '海运订单id',
  `sea_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海运订单id',
  `cut_replenish_time` datetime(0) NULL DEFAULT NULL COMMENT '截补料时间',
  `cabinet_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号',
  `paper_strip_seal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_bill_of_lading` int(10) NULL DEFAULT NULL COMMENT '是否已提单',
  `imp_and_exp_type` int(10) NULL DEFAULT NULL COMMENT '进出口类型(1：进口，2：出口)',
  `terms` int(10) NULL DEFAULT NULL COMMENT '贸易方式(0:FOB,1:CIF,2:DAP,3:FAC,4:DDU,5:DDP)',
  `port_departure_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运港代码',
  `port_destination_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的港代码',
  `good_time` datetime(0) NULL DEFAULT NULL COMMENT '货好时间',
  `is_freight_collect` tinyint(1) NULL DEFAULT NULL COMMENT '运费是否到付(1代表true,0代表false)',
  `is_other_expenses_paid` tinyint(1) NULL DEFAULT NULL COMMENT '其他费用是否到付(1代表true,0代表false)',
  `is_dangerous_goods` tinyint(1) NULL DEFAULT NULL COMMENT '是否危险品(1代表true,0代表false)',
  `is_charged` tinyint(1) NULL DEFAULT NULL COMMENT '是否带电(1代表true,0代表false)',
  `transit_port_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中转港',
  `cabinet_type` int(50) NULL DEFAULT NULL COMMENT '柜型类型',
  `cabinet_type_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜型类型名字',
  `main_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主单号',
  `sub_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分单号',
  `bill_lading_weight` double NULL DEFAULT NULL COMMENT '提单重量',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单文件路径(多个逗号隔开)',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单文件名称(多个逗号隔开)',
  `is_release_order` int(10) NULL DEFAULT NULL COMMENT '是否已放单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 0 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '海运补料表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
