SQL脚本


-- 2020年10月30日李达荣，功能描述：基础数据费用类型
CREATE TABLE `cost_genre` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(255) NOT NULL COMMENT '费用类型代码',
  `name` varchar(255) NOT NULL COMMENT '费用类型名称',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '状态（0无效 1有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='基础数据费用类型';

-- 2020年10月31日李达荣，功能描述：删除供应商表中票后字段
ALTER TABLE `supplier_info`
DROP COLUMN `after_the_vote`;

-- 2020年11月2日李达荣，功能描述：合同信息表增加合同类型和绑定业务id
ALTER TABLE `contract_info`
ADD COLUMN `type` char(5) NOT NULL COMMENT '合同类型' AFTER `updated_time`,
ADD COLUMN `bind_id` bigint(20) NOT NULL COMMENT '合同绑定的业务id' AFTER `type`;

-- 2020年11月2日李达荣，功能描述：系统用户表增加描述
ALTER TABLE `system_user`
MODIFY COLUMN `user_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1-用户 2-客户 3-供应商' AFTER `audit_status`;

-- 2020年11月4日李达荣，功能描述：车辆管理增加 牌头电话、更新人、寮步密码、企业代码字段
ALTER TABLE `vehicle_info`
DROP COLUMN `pt_phone`,
DROP COLUMN `update_user`,
DROP COLUMN `stepping_code`,
DROP COLUMN `enterprise_code`,
ADD COLUMN `pt_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '牌头电话' AFTER `pt_fax`,
MODIFY COLUMN `car_type` int(10) NOT NULL COMMENT '车辆类型(1吨车 2柜车)' AFTER `pt_fax`,
ADD COLUMN `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人' AFTER `update_time`,
ADD COLUMN `stepping_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '寮步密码' AFTER `update_user`,
ADD COLUMN `enterprise_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业代码' AFTER `stepping_code`;

-- 2020年11月4日李达荣，功能描述：司机管理增加 密码（微信使用）、更新人字段
ALTER TABLE `driver_info`
CHANGE COLUMN `up_time` `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间' AFTER `create_time`,
ADD COLUMN `update_user` varchar(50) NULL COMMENT '更新人' AFTER `update_time`,
ADD COLUMN `password` varchar(50) NULL COMMENT '密码（微信登录使用）' AFTER `update_user`;

-- 2020年11月4日李达荣，功能描述：添加客户地址表
CREATE TABLE `customer_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '地址id',
  `type` char(10) NOT NULL COMMENT '地址类型（0 提货地址 1送货地址）',
  `customer_id` bigint(20) NOT NULL COMMENT '客户id',
  `contacts` varchar(50) NOT NULL COMMENT '联系人',
  `phone` varchar(20) NOT NULL COMMENT '联系电话',
  `province` varchar(20) NOT NULL COMMENT '省',
  `city` varchar(20) NOT NULL COMMENT '市',
  `area` varchar(20) NOT NULL COMMENT '区',
  `address` varchar(100) NOT NULL COMMENT '详细地址',
  `postcode` varchar(20) DEFAULT NULL COMMENT '邮编',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '状态（0无效 1有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='客户地址';