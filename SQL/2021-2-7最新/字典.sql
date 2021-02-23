

CREATE TABLE `dict` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '字典类型主键',
  `value` varchar(255) NOT NULL COMMENT '类型值',
  `code` varchar(255) NOT NULL COMMENT '代码',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态(0无效 1有效)',
  `dict_type_code` varchar(20) NOT NULL COMMENT '字典类型code',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='字典';


CREATE TABLE `dict_type` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '字典类型主键',
  `name` varchar(255) NOT NULL COMMENT '类型名称',
  `code` varchar(255) NOT NULL COMMENT '类型代码',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` char(1) DEFAULT '1' COMMENT '状态(0无效 1有效)',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COMMENT='字典类型';