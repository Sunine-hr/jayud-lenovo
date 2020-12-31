
-- 2020年12月15日李达荣，功能描述：中港订单扩展字段表
ALTER TABLE `logistics_track`
ADD COLUMN `type` int(10) NULL DEFAULT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输)' AFTER `created_user`;