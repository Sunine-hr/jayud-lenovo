-- 2020年12月15日李达荣，功能描述：中港订单扩展字段表
CREATE TABLE `tms_extension_field` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint(20) DEFAULT NULL COMMENT '业务主键',
  `third_party_unique_sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '第三方唯一标志',
  `business_table` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务表(例如:order_transport)',
  `value` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '数据(json格式)',
  `type` int(10) NOT NULL COMMENT '类型(0:vivo,待定)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述(也可以当key值使用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='中港运输订单扩展字段表';