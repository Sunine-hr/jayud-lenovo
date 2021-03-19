
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `trailer_order`;
CREATE TABLE `trailer_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拖车订单编号',
  `main_order_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主订单编号',
  `status` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3拖车提柜,TT_4拖车到仓,TT_5拖车离仓,TT_6拖车过磅,TT_7确认还柜)',
  `process_status` int(10) NULL DEFAULT NULL COMMENT '流程状态(0:进行中,1:完成,2:草稿,3.关闭)',
  `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id',
  `legal_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单法人名称',
  `imp_and_exp_type` int(10) NULL DEFAULT NULL COMMENT '进出口类型(1：进口，2：出口)',
  `unit_code` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位code',
  `unit_code_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算单位姓名',
  `port_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运港/目的港代码',
  `cabinet_size` bigint(20) NULL DEFAULT NULL COMMENT '车型尺寸id',
  `bill_of_lading` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提运单',
  `bol_file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提运单附件路径',
  `bol_file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提运单附件名称',
  `paper_strip_seal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条',
  `pss_file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条附件路径',
  `pss_file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封条附件名称',
  `cabinet_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号',
  `cn_file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号附件路径',
  `cn_file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '柜号附件名称',
  `so` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SO',
  `so_file_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SO附件路径',
  `so_file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'SO附件名称',
  `arrival_time` datetime(0) NULL DEFAULT NULL COMMENT '到港时间',
  `closing_warehouse_time` datetime(0) NULL DEFAULT NULL COMMENT '截仓期时间',
  `time_counter_rent` datetime(0) NULL DEFAULT NULL COMMENT '截柜租时间',
  `open_time` datetime(0) NULL DEFAULT NULL COMMENT '开仓时间',
  `cutting_replenishing_time` datetime(0) NULL DEFAULT NULL COMMENT '截补料时间',
  `is_weighed` tinyint(1) NULL DEFAULT NULL COMMENT '是否过磅(1代表true,0代表false)',
  `is_make_up` tinyint(1) NULL DEFAULT NULL COMMENT '是否做补料(1代表true,0代表false)',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `need_input_cost` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否需要录入费用(0:false,1:true)',
  `order_taker` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接单人(登录用户名)',
  `receiving_orders_date` datetime(0) NULL DEFAULT NULL COMMENT '接单日期',
  `closing_time` datetime(0) NULL DEFAULT NULL COMMENT '截关时间',
  `release_time` datetime(0) NULL DEFAULT NULL COMMENT '放行时间',
  `process_description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拖车订单表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
