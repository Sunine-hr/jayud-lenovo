

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `order_type_number`;
CREATE TABLE `order_type_number`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型(参考OrderTypeEnum)',
  `date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前日期时间',
  `number` int(20) NULL DEFAULT NULL COMMENT '当前数值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单单号记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
