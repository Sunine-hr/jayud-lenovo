-- 追加贸易方式
ALTER TABLE `air_order`
MODIFY COLUMN `terms` int(0) NULL DEFAULT NULL COMMENT '贸易方式(0:CIF,1:DDU,2:FOB,3:DDP,4:CFR,5:CPT,6:CNF,7:CIP,8:FCA)' AFTER `imp_and_exp_type`;