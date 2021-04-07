SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `supplier_rela_legal`;
CREATE TABLE `supplier_rela_legal`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `supplier_info_id` bigint(0) NOT NULL COMMENT '供应商表ID',
  `legal_entity_id` bigint(0) NOT NULL COMMENT '法人主体ID',
  `created_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '供应商对应法人主体表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
