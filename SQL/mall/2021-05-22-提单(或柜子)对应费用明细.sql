CREATE TABLE `jayud_shop`.`fee_cope_with`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `qie`           int(10) NULL DEFAULT NULL COMMENT '提单id(ocean_bill id)/柜子id(ocean_counter id)',
    `cost_code`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用代码(cost_item cost_code)',
    `cost_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用名称(cost_item cost_name)',
    `supplier_id`   int(10) NULL DEFAULT NULL COMMENT '供应商id(supplier_info id)',
    `calculate_way` int(10) NULL DEFAULT 1 COMMENT '计算方式(1自动 2手动)',
    `count`         int(10) NULL DEFAULT NULL COMMENT '数量',
    `unit`          int(20) NULL DEFAULT 1 COMMENT '数量单位(1公斤 2方 3票 4柜)',
    `source`        int(20) NULL DEFAULT NULL COMMENT '来源(1计费重2固定)',
    `unit_price`    decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
    `cid`           int(11) NULL DEFAULT NULL COMMENT '币种(currency_info id)',
    `amount`        decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
    `remarks`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
    `business_type` int(10) NULL DEFAULT NULL COMMENT '业务类型(1提单费用 2柜子费用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '提单(或柜子)对应费用明细' ROW_FORMAT = DYNAMIC;

