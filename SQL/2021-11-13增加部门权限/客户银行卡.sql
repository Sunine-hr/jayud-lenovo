CREATE TABLE `bank_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` bigint(20) NOT NULL COMMENT '客户id',
  `account_bank` varchar(50) NOT NULL COMMENT '开户行',
  `num` varchar(50) NOT NULL COMMENT '开户行编号',
  `account` varchar(50) NOT NULL COMMENT '账号',
  `currency_code` varchar(50) NOT NULL COMMENT '币种类别',
  `payment_method` varchar(50) DEFAULT NULL COMMENT '收付款方式',
  `exchange_line_num` varchar(50) DEFAULT NULL COMMENT '交换行号',
  `collection_contract_no` varchar(50) DEFAULT NULL COMMENT '托收合同号',
  `status` int(5) DEFAULT '1' COMMENT '状态（0 禁用 1启用 2删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` int(5) NOT NULL COMMENT '类型(1:客户,2:供应商)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4;