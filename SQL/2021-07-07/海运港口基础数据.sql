ALTER TABLE `sea_port`
ADD COLUMN `country` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家' AFTER `status`,
ADD COLUMN `route` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '航线' AFTER `country`,
ADD COLUMN `chinese_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '港口中文名' AFTER `route`;

ALTER TABLE `sea_port`
MODIFY COLUMN `code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ;

ALTER TABLE `sea_port`
MODIFY COLUMN `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL ;


ALTER TABLE `goods`
MODIFY COLUMN `total_weight` double(20, 4) NULL DEFAULT NULL COMMENT '总重量' AFTER `size`,
MODIFY COLUMN `volume` double(20, 4) NULL DEFAULT NULL COMMENT '体积' AFTER `total_weight`;
