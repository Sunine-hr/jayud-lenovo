CREATE TABLE `order_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `main_order_no` varchar(200) DEFAULT NULL COMMENT '主订单编号',
  `order_no` varchar(200) DEFAULT NULL COMMENT '子订单编号',
  `file_path` varchar(1000)  DEFAULT NULL COMMENT '附件文件路径(多个逗号隔开)',
  `file_name` varchar(1000)  DEFAULT NULL COMMENT '附件文件名称(多个逗号隔开)',
  `remarks` varchar(255) DEFAULT NULL COMMENT '描述(可以当key使用)',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `status` int(10) DEFAULT NULL COMMENT '状态(0:禁用,1:启用)',
  PRIMARY KEY (`id`)
) COMMENT='订单附件';