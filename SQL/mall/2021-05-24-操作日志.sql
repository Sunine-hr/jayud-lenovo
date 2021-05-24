SET
FOREIGN_KEY_CHECKS=0;

CREATE TABLE `jayud_shop`.`business_log`
(
    `id`                 bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `operation_time`     datetime(0) NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '操作时间',
    `user_id`            int(11) NULL DEFAULT NULL COMMENT '操作人id(system_user id)',
    `user_name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人name(system_user name)',
    `business_tb`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务表tb',
    `business_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务表(中文)name',
    `business_operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务操作(insert update delete)',
    `operation_front`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作前(text)',
    `operation_after`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作后(text)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务日志表' ROW_FORMAT = Dynamic;

ALTER TABLE `jayud_shop`.`order_case` MODIFY COLUMN `asn_volume` decimal (10, 3) NULL DEFAULT NULL COMMENT '预报长宽高计算得到的体积，单位m³' AFTER `asn_weight`;

ALTER TABLE `jayud_shop`.`order_case` MODIFY COLUMN `wms_volume` decimal (10, 3) NULL DEFAULT NULL COMMENT '仓库计量长宽高得到的体积，单位m³' AFTER `wms_weight`;

ALTER TABLE `jayud_shop`.`order_case` MODIFY COLUMN `confirm_volume` decimal (10, 3) NULL DEFAULT NULL COMMENT '最终确认体积，单位 m³' AFTER `confirm_weight`;

ALTER TABLE `jayud_shop`.`quoted_file` MODIFY COLUMN `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT '创建时间' AFTER `status`;

SET
FOREIGN_KEY_CHECKS=1;