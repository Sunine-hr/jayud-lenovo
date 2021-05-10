ALTER TABLE `jayud_shop`.`delivery_address`
    ADD COLUMN `region_code` varchar(255) NULL COMMENT '区县代码' AFTER `city_name`,
ADD COLUMN `region_name` varchar(255) NULL COMMENT '区县名称' AFTER `region_code`;


ALTER TABLE `jayud_shop`.`shipping_area`
DROP
COLUMN `state_code`,
DROP
COLUMN `pid`,
DROP
COLUMN `pname`,
DROP
COLUMN `cid`,
DROP
COLUMN `cname`,
ADD COLUMN `country_code` varchar(255) NULL COMMENT '国家代码' AFTER `address_thirdly`,
ADD COLUMN `country_name` varchar(255) NULL COMMENT '国家名称' AFTER `country_code`,
ADD COLUMN `state_code` varchar(255) NULL COMMENT '省/州代码' AFTER `country_name`,
ADD COLUMN `state_name` varchar(255) NULL COMMENT '省/州名称' AFTER `state_code`,
ADD COLUMN `city_code` varchar(255) NULL COMMENT '城市代码' AFTER `state_name`,
ADD COLUMN `city_name` varchar(255) NULL COMMENT '城市名称' AFTER `city_code`,
ADD COLUMN `region_code` varchar(255) NULL COMMENT '区县代码' AFTER `city_name`,
ADD COLUMN `region_name` varchar(255) NULL COMMENT '区县名称' AFTER `region_code`;