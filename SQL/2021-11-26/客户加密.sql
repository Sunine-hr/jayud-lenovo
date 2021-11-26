
-- 客户对外接口秘钥表
CREATE TABLE `client_secret_key` (
 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `app_id` varchar(46) DEFAULT NULL COMMENT '应用appID',
  `customer_info_id` int(11) DEFAULT '0' COMMENT '客户ID',
  `app_secret` varchar(1024) DEFAULT NULL COMMENT '应用公钥密钥',
  `app_private_secret` varchar(1024) DEFAULT NULL COMMENT '应用私钥密钥',
  `status` int(5) DEFAULT '1' COMMENT '状态（0 禁用 1启用 2删除）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='客户对外接口秘钥表';


