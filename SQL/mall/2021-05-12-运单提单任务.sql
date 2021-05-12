SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`customer` MODIFY COLUMN `operation_team_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运营(服务)小组id(operation_team id),多个用逗号分隔' AFTER `salesman_id`;

ALTER TABLE `jayud_shop`.`customer` DROP COLUMN `operation_team_two`;

ALTER TABLE `jayud_shop`.`ocean_bill` MODIFY COLUMN `operation_team_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运营(服务)小组id(operation_team id),多个用逗号分隔' AFTER `task_id`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `country_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名称' AFTER `country_code`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `state_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省/州代码' AFTER `country_name`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `state_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省/州名称' AFTER `state_code`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `city_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市代码' AFTER `state_name`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `city_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市名称' AFTER `city_code`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `region_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县代码' AFTER `city_name`;

ALTER TABLE `jayud_shop`.`shipping_area` MODIFY COLUMN `region_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区县名称' AFTER `region_code`;

SET FOREIGN_KEY_CHECKS=1;