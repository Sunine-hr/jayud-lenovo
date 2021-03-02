ALTER TABLE `goods`
MODIFY COLUMN `business_type` int(10) NOT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输,3:报关,4:海运,5:内陆)参考BusinessTypeEnum' AFTER `business_id`;