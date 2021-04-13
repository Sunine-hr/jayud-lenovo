CREATE TABLE `order_cost_template_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_cost_template_id` bigint DEFAULT NULL COMMENT '费用模板id',
  `cost_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '费用名称code',
  `cost_type_id` bigint DEFAULT NULL COMMENT '费用类别',
  `cost_genre_id` bigint DEFAULT NULL COMMENT '费用类型',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位',
  `unit_price` decimal(10,4) DEFAULT NULL COMMENT '单价',
  `number` decimal(10,4) DEFAULT NULL COMMENT '数量',
  `currency_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种代码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `currency` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='费用模板详情';



CREATE TABLE `order_cost_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `status` int DEFAULT NULL COMMENT '状态(0禁用 1启用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `type` int NOT NULL COMMENT '费用类型(0:应收,1:应付)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='费用模板';