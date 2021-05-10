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



ALTER TABLE `jayud_shop`.`goods_service_cost`
    ADD UNIQUE INDEX `unique_service_id_and_good_id`(`service_id`, `good_id`) USING BTREE COMMENT '服务id和商品id不能重复';


ALTER TABLE `jayud_shop`.`order_pick`
    ADD COLUMN `from_country` varchar(255) NULL COMMENT '起运地-国家' AFTER `transport_id`,
ADD COLUMN `from_province` varchar(255) NULL COMMENT '起运地-省州' AFTER `from_country`,
ADD COLUMN `from_city` varchar(255) NULL COMMENT '起运地-城市' AFTER `from_province`,
ADD COLUMN `from_region` varchar(255) NULL COMMENT '起运地-区县' AFTER `from_city`,
ADD COLUMN `to_country` varchar(255) NULL COMMENT '目的地-国家' AFTER `from_region`,
ADD COLUMN `to_province` varchar(255) NULL COMMENT '目的地-省州' AFTER `to_country`,
ADD COLUMN `to_city` varchar(255) NULL COMMENT '目的地-城市' AFTER `to_province`,
ADD COLUMN `to_region` varchar(255) NULL COMMENT '目的地-区县' AFTER `to_city`,
ADD COLUMN `unit_price` decimal(10, 2) NULL COMMENT '单价' AFTER `to_region`,
ADD COLUMN `cid` int NULL COMMENT '币种(currency_info id)' AFTER `unit_price`,
ADD COLUMN `status` varchar(255) NULL COMMENT '状态(0无效 1有效)' AFTER `cid`,
ADD COLUMN `count` decimal(20, 2) NULL COMMENT '数量' AFTER `status`,
ADD COLUMN `fee_remark` varchar(255) NULL COMMENT '费用备注' AFTER `count`;


ALTER TABLE `jayud_shop`.`order_pick`
    ADD COLUMN `amount` decimal(20, 2) NULL COMMENT '金额' AFTER `fee_remark`;


ALTER TABLE `jayud_shop`.`order_pick`
    MODIFY COLUMN `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '金额' AFTER `count`;

ALTER TABLE `jayud_shop`.`order_pick`
    ADD COLUMN `unit` int(10) NULL COMMENT '单位(1公斤 2方 3票 4柜)' AFTER `cid`,
MODIFY COLUMN `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)' AFTER `cid`;


ALTER TABLE `jayud_shop`.`order_pick`
    MODIFY COLUMN `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)' AFTER `unit`;


