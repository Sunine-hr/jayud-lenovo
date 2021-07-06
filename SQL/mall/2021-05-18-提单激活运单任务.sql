SET
FOREIGN_KEY_CHECKS=0;

CREATE TABLE `jayud_shop`.`bill_order_relevance`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `bill_id`     int(11) NULL DEFAULT NULL COMMENT '提单id(ocean_bill id)',
    `bill_no`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单号(ocean_bill order_id)',
    `order_id`    bigint(20) NULL DEFAULT NULL COMMENT '订单id',
    `order_no`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号(order_info order_no)',
    `is_inform`   char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否通知运单物流轨迹(1通知 2不通知)',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提单关联订单(任务通知表)' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS=1;