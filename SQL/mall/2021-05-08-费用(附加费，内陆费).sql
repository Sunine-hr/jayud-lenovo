SET
FOREIGN_KEY_CHECKS=0;

CREATE TABLE `goods_service_cost`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `service_id`    bigint(20) NULL DEFAULT NULL COMMENT '服务id(service_group id)',
    `good_id`       bigint(20) NULL DEFAULT NULL COMMENT '商品id(customer_goods id)',
    `customer_id`   bigint(20) NULL DEFAULT NULL COMMENT '客户ID(customer id)',
    `name_cn`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品名(中文名)(customer_goods name_cn)',
    `unit_price`    decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
    `cid`           int(10) NULL DEFAULT NULL COMMENT '币种(currency_info id)',
    `unit`          int(10) NULL DEFAULT 1 COMMENT '单位(1公斤 2方 3票 4柜)',
    `status`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `service_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称(service_group code_name)',
    `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称(customer company)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品服务费用表' ROW_FORMAT = Dynamic;

CREATE TABLE `inland_fee_cost`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `cost_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'JYD-REC-COS-00002' COMMENT '内陆费代码(cost_item cost_code)',
    `cost_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '内陆费' COMMENT '内陆费名称(cost_item cost_name)',
    `from_country`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运地-国家',
    `from_province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运地-省州',
    `from_city`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运地-城市',
    `from_region`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起运地-区县',
    `to_country`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地-国家',
    `to_province`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地-省州',
    `to_city`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地-城市',
    `to_region`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的地-区县',
    `unit_price`    decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
    `cid`           int(10) NULL DEFAULT NULL COMMENT '币种(currency_info id)',
    `unit`          int(10) NULL DEFAULT 1 COMMENT '单位(1公斤 2方 3票 4柜)',
    `status`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '内陆费费用表' ROW_FORMAT = Dynamic;

ALTER TABLE `quotation_template`
    ADD COLUMN `minimum_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '最低数量' AFTER `volume_unit`;

ALTER TABLE `quotation_template` DROP COLUMN `data_type`;

ALTER TABLE `template_cope_receivable` DROP COLUMN `minimum_quantity`;

ALTER TABLE `template_cope_with` DROP COLUMN `minimum_quantity`;

SET
FOREIGN_KEY_CHECKS=1;