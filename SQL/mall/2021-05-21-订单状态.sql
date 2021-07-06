SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`order_info`
    ADD COLUMN `front_status_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端状态代码\r\n    草稿:0\r\n    补资料:9\r\n    已下单:10\r\n    已收货:20\r\n    转运中:30\r\n    已签收:40\r\n    已完成:50\r\n    已取消:-1' AFTER `sales_commission`;

ALTER TABLE `jayud_shop`.`order_info`
    ADD COLUMN `front_status_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端状态名称' AFTER `front_status_code`;

ALTER TABLE `jayud_shop`.`order_info`
    ADD COLUMN `after_status_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端状态代码\r\n    草稿:0\r\n    补资料:9\r\n    已下单:10\r\n        -- 内部小状态，这个不是流程状态\r\n        -- 已审单\r\n        -- 未审单\r\n    已收货:20\r\n    订单确认:30\r\n    已签收:40\r\n    已完成:50\r\n    已取消:-1' AFTER `front_status_name`;

ALTER TABLE `jayud_shop`.`order_info`
    ADD COLUMN `after_status_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端状态名称' AFTER `after_status_code`;

ALTER TABLE `jayud_shop`.`order_info` DROP COLUMN `status`;

ALTER TABLE `jayud_shop`.`order_info` DROP COLUMN `status_name`;

CREATE TABLE `jayud_shop`.`order_interior_status`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `order_id`             bigint(20) NULL DEFAULT NULL COMMENT '订单id(order_info id)',
    `order_no`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号(order_info order_no)',
    `main_status_type`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主状态类型(front前端 after后端)',
    `main_status_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主状态代码',
    `main_status_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主状态名称',
    `interior_status_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部状态唯一码',
    `interior_status_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部状态名称',
    `status_flag`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态标志',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单内部状态表(非流程状态)' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS=1;



-- fab_warehouse
SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `country_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家代码' AFTER `area_group`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `country_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名称' AFTER `country_code`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `state_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省/州名称' AFTER `state_code`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `city_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市代码' AFTER `state_name`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `city_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市名称' AFTER `city_code`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `region_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县代码' AFTER `city_name`;

ALTER TABLE `jayud_shop`.`fab_warehouse`
    ADD COLUMN `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县名称' AFTER `region_code`;

ALTER TABLE `jayud_shop`.`fab_warehouse` MODIFY COLUMN `state_code` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省/州代码' AFTER `country_name`;

ALTER TABLE `jayud_shop`.`fab_warehouse` DROP COLUMN `pid`;

ALTER TABLE `jayud_shop`.`fab_warehouse` DROP COLUMN `pname`;

ALTER TABLE `jayud_shop`.`fab_warehouse` DROP COLUMN `cid`;

ALTER TABLE `jayud_shop`.`fab_warehouse` DROP COLUMN `cname`;

SET
FOREIGN_KEY_CHECKS=1;