-- 2021-1-20 物流轨迹更改业务类型描述
ALTER TABLE `logistics_track`
MODIFY COLUMN `type` int(10) NULL DEFAULT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输....)参考BusinessTypeEnum' AFTER `created_user`;

-- 2021-1-20 商品更改业务类型描述
ALTER TABLE `goods`
MODIFY COLUMN `business_type` int(10) NOT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输...)参考BusinessTypeEnum' AFTER `business_id`;

-- 2021-1-20 商品地址更改业务类型描述
ALTER TABLE `order_address`
MODIFY COLUMN `business_type` int(10) NULL DEFAULT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输...)参考BusinessTypeEnum' AFTER `type`;