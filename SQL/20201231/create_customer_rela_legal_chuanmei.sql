--20201203 二期优化
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer_rela_legal
-- ----------------------------
DROP TABLE IF EXISTS `customer_rela_legal`;
CREATE TABLE `customer_rela_legal`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_info_id` bigint(20) NOT NULL COMMENT '客户表ID',
  `legal_entity_id` bigint(20) NOT NULL COMMENT '法人主体ID',
  `created_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `created_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;