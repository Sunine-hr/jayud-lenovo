--部门新增legal_id字段
alter table department add column legal_id bigint(20) comment '法人主体id';

--供应商增加法人主体id字段
alter table supplier_info add column legal_entity_id bigint(20) comment '法人主体id';

--新建用户与法人主体关系表
DROP TABLE IF EXISTS `system_user_legal`;
CREATE TABLE `system_user_legal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `legal_id` bigint(20) NULL DEFAULT NULL COMMENT '法人主体id',
  `system_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户信息id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Fixed;

SET FOREIGN_KEY_CHECKS = 1;