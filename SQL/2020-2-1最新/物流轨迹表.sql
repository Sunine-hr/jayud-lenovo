

-- 业务类型改成必填
ALTER TABLE `logistics_track`
MODIFY COLUMN `type` int(10) NOT NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输)参考BusinessTypeEnum' AFTER `created_user`;