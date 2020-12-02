SQL脚本


-- 2020年12月2日李达荣，功能描述：物流轨迹表增加区分字段,区分业务类型
ALTER TABLE `jayud-test`.`logistics_track`
    ADD COLUMN `type` int(10) NULL COMMENT '业务类型(0:空运,1:纯报关,2:中港运输)' AFTER `created_user`;