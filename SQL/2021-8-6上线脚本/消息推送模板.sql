CREATE TABLE `message_push_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `num` varchar(50) NOT NULL COMMENT '消息编号(规则MT+4位数序列号)',
  `msg_name` varchar(255) NOT NULL COMMENT '消息名称',
  `type` tinyint(10) NOT NULL COMMENT '消息类型(1:操作状态,2:客户状态)',
  `sub_type` varchar(20) NOT NULL COMMENT '业务模块',
  `trigger_status` varchar(20) NOT NULL COMMENT '触发状态',
  `send_time_type` tinyint(10) NOT NULL COMMENT '发送时间类型(1:立即,2:延后)',
  `send_time` int(20) DEFAULT '0' COMMENT '延迟天数',
  `time_unit` varchar(20) DEFAULT NULL COMMENT '时间单位',
  `template_content` varchar(500) NOT NULL COMMENT '发送模板内容',
  `content` varchar(500) NOT NULL COMMENT '发送内容',
  `rep_param` varchar(500) NOT NULL COMMENT '替换参数(多个参数使用逗号隔开)',
  `sql_select` text NOT NULL COMMENT 'sql查询语句',
  `template_title` varchar(255) DEFAULT NULL COMMENT '模板标题',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `post` varchar(255) DEFAULT NULL COMMENT '接收岗位(多个参数使用逗号隔开)',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(5) DEFAULT '1' COMMENT '状态(0禁用 1启用)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='消息推送模板';


CREATE TABLE `msg_push_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `recipient_name` varchar(500) NOT NULL COMMENT '接收人名称',
  `recipient_id` bigint(20) NOT NULL COMMENT '接收人id',
  `type` tinyint(10) NOT NULL COMMENT '消息类型(1:操作状态,2:客户状态)',
  `post` varchar(255) DEFAULT NULL COMMENT '岗位',
  `receiving_status` varchar(255) DEFAULT NULL COMMENT '接收状态',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(5) DEFAULT '1' COMMENT '状态(0禁用 1启用)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='消息推送列表';

CREATE TABLE `msg_push_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `recipient_name` varchar(500) NOT NULL COMMENT '接收人名称',
  `recipient_id` bigint(20) NOT NULL COMMENT '接收人id',
  `type` tinyint(10) NOT NULL COMMENT '消息类型(1:操作状态,2:客户状态)',
  `post` varchar(50) DEFAULT NULL COMMENT '岗位',
  `receiving_status` varchar(255) DEFAULT NULL COMMENT '接收状态',
  `receive_content` varchar(255) DEFAULT NULL COMMENT '接收内容',
  `receiving_mode` varchar(50) DEFAULT NULL COMMENT '接收方式',
  `receiving_account` varchar(255) DEFAULT NULL COMMENT '接收账号',
  `num` int(20) DEFAULT NULL COMMENT '发送次数',
  `initial_time` datetime DEFAULT NULL COMMENT '初始时间',
  `send_time_type` tinyint(10) NOT NULL COMMENT '发送时间类型(1:立即,2:延后)',
  `delay_time` int(20) DEFAULT '0' COMMENT '延迟天数',
  `time_unit` varchar(20) DEFAULT NULL COMMENT '时间单位',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `status` tinyint(10) DEFAULT NULL COMMENT '发送状态(1:发送成功,2:发送失败,3:待发送)',
  `err_msg` text COMMENT '错误信息',
  `msg_channel_type` int(20) DEFAULT NULL COMMENT '消息渠道类型(1邮件)',
  `template_content` varchar(500) NOT NULL COMMENT '发送模板内容',
  `sql_select` text NOT NULL COMMENT 'sql查询语句',
  `template_title` varchar(255) DEFAULT NULL COMMENT '模板标题',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='消息推送记录';

CREATE TABLE `msg_user_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(10) NOT NULL COMMENT '消息渠道类型(1邮件)',
  `name` varchar(255) NOT NULL COMMENT '消息渠道名称',
  `account` varchar(255) DEFAULT NULL COMMENT '渠道账号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `is_select` tinyint(1) NOT NULL COMMENT '是否默认',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息渠道';

CREATE TABLE `binding_msg_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `msg_list_id` bigint(20) NOT NULL COMMENT '消息列表id',
  `post` varchar(255) DEFAULT NULL COMMENT '岗位',
  `template_id` bigint(20) DEFAULT NULL COMMENT '模板id',
  `type` tinyint(10) NOT NULL COMMENT '消息类型(1:操作状态,2:客户状态)',
  `receiving_mode` varchar(255) DEFAULT NULL COMMENT '接收方式',
  `receiving_account` varchar(255) DEFAULT NULL COMMENT '接收账号',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人(登录用户)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(5) DEFAULT '1' COMMENT '状态(0禁用 1启用)',
  `self_regarding` tinyint(1) DEFAULT '0' COMMENT '是否关注自己',
  `msg_channel` varchar(50) DEFAULT NULL COMMENT '消息渠道(邮件,微信...)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='绑定消息模板';

CREATE TABLE `system_conf` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int(20) DEFAULT NULL COMMENT '系统类型(1:邮箱)',
  `conf_data` text COMMENT '配置源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `status` int(10) unsigned DEFAULT NULL COMMENT '状态(0禁用 1启用)',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='消息系统配置';