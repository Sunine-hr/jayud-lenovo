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

-- 2020年11月5日李达荣，功能描述：中转仓库更改字段
ALTER TABLE `warehouse_info`
DROP COLUMN `company_name`,
DROP COLUMN `contact_phone`,
DROP COLUMN `country_code`,
MODIFY COLUMN `warehouse_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中转仓库代码' AFTER `id`,
MODIFY COLUMN `warehouse_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中转仓库名称' AFTER `warehouse_code`,
ADD COLUMN `area` varchar(50) NULL COMMENT '区' AFTER `city`;
ALTER TABLE `jayud-test`.`warehouse_info`
CHANGE COLUMN `area` `area_code` bigint(20) NULL DEFAULT NULL COMMENT '区' AFTER `city_code`;

-- 2020年11月5日李达荣，功能描述：中转仓库主键更long类型
ALTER TABLE `warehouse_info`
MODIFY COLUMN `id` bigint(11) NOT NULL AUTO_INCREMENT FIRST;

-- 2020年11月6日李达荣，功能描述：供应商更改字段名
ALTER TABLE `supplier_info`
CHANGE COLUMN `product_classify_id` `product_classify_ids` bigint(20) NOT NULL COMMENT '产品分类id集合（多个逗号隔开）' AFTER `supplier_code`;
ALTER TABLE `jayud_platform`.`supplier_info`
MODIFY COLUMN `product_classify_ids` varchar(50) NOT NULL COMMENT '产品分类id集合（多个逗号隔开）' AFTER `supplier_code`;

-- 2020年11月6日李达荣，功能描述：车辆表修改字段类型，增加车辆吨位字段
ALTER TABLE `vehicle_info`
MODIFY COLUMN `weight` varchar(50) NULL DEFAULT NULL COMMENT '吉车重量' AFTER `card_number`,
ADD COLUMN `vehicle_tonnage` varchar(50) NULL COMMENT '车辆吨位' AFTER `enterprise_code`;

-- 2020年11月6日李达荣，功能描述：法人主体加字段
ALTER TABLE `legal_entity`
ADD COLUMN `legal_en_name` varchar(50) NULL COMMENT '英文名' AFTER `updated_time`,
ADD COLUMN `phone` varchar(20) NULL COMMENT '电话' AFTER `legal_en_name`,
ADD COLUMN `fax` varchar(50) NULL COMMENT '传真' AFTER `phone`,
ADD COLUMN `address` varchar(50) NULL COMMENT '地址' AFTER `fax`,
ADD COLUMN `bank` varchar(50) NULL COMMENT '开户银行' AFTER `address`,
ADD COLUMN `account_open` varchar(50) NULL COMMENT '开户账户' AFTER `bank`,
ADD COLUMN `tax_identification_num` varchar(50) NULL COMMENT '纳税识别号信息' AFTER `account_open`;

-- 2020年11月6日李达荣，功能描述：中转仓更改字段类型
ALTER TABLE `warehouse_info`
MODIFY COLUMN `state_code` bigint(20) NULL DEFAULT NULL COMMENT '省' AFTER `address`,
MODIFY COLUMN `city_code` bigint(20) NULL DEFAULT NULL COMMENT '市' AFTER `state_code`,
MODIFY COLUMN `area_code` bigint(20) NULL DEFAULT NULL COMMENT '区' AFTER `city_code`;



-- 2020年11月6日李达荣，功能描述：主订单基础数据表增加接单法人id
-- ALTER TABLE `jayud-test`.`order_info`
-- ADD COLUMN `legal_entity_id` bigint(20) NULL DEFAULT NULL COMMENT '接单法人id' AFTER `up_user`;


-- 2020年11月10日李达荣，功能描述：车辆表增加字段
ALTER TABLE `vehicle_info`
ADD COLUMN `file_name` varchar(255) NULL COMMENT '附件名称，多个时用逗号隔开' AFTER `vehicle_tonnage`;

-- 2020年11月10日李达荣，功能描述：供应商设置非必填
ALTER TABLE `supplier_info`
MODIFY COLUMN `buyer_id` bigint(20) NULL COMMENT '采购人员id' AFTER `rate`;


-- 2020年11月10日李达荣，功能描述：客户维护地址字段类型更改
ALTER TABLE `customer_address`
MODIFY COLUMN `province` int(20) NOT NULL COMMENT '省主键' AFTER `phone`,
MODIFY COLUMN `city` int(20) NOT NULL COMMENT '市主键' AFTER `province`,
MODIFY COLUMN `area` int(20) NULL DEFAULT NULL COMMENT '区主键' AFTER `city`;

-- 2020年11月10日李达荣，功能描述：送货/收货地址增加字段
ALTER TABLE `delivery_address`
ADD COLUMN `type` char(10) NULL COMMENT '地址类型（0 提货地址 1送货地址）' AFTER `create_time`,
ADD COLUMN `province` int(20) NOT NULL COMMENT '省主键' AFTER `type`,
ADD COLUMN `city` int(20) NOT NULL COMMENT '市主键' AFTER `province`,
ADD COLUMN `area` int(20) NULL DEFAULT NULL COMMENT '区主键' AFTER `city`;

-- sql 以上都同步到测试服务器