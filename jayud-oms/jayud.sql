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
ALTER TABLE `jayud-test`.`supplier_info`
DROP COLUMN `after_the_vote`;

-- 2020年11月2日李达荣，功能描述：合同信息表增加合同类型和绑定业务id
ALTER TABLE `jayud-test`.`contract_info`
ADD COLUMN `type` char(5) NOT NULL COMMENT '合同类型' AFTER `updated_time`,
ADD COLUMN `bind_id` bigint(20) NOT NULL COMMENT '合同绑定的业务id' AFTER `type`;

-- 2020年11月2日李达荣，功能描述：系统用户表增加描述
ALTER TABLE `jayud-test`.`system_user`
MODIFY COLUMN `user_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1-用户 2-客户 3-供应商' AFTER `audit_status`;