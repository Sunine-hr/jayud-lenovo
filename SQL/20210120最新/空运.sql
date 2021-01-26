-- 2021-1-23 李达荣：修改贸易类型
ALTER TABLE `air_order`
MODIFY COLUMN `terms` int(10) NULL DEFAULT NULL COMMENT '贸易方式(0:CIF,1:DDU,2:FOB,3:DDP,4:CFR,5:CPT,6:CNF,7:CIP)' AFTER `imp_and_exp_type`;

-- 2021-1-26 李达荣：修改为登录用户名称
ALTER TABLE `general_api_log`
DROP COLUMN `user_id`,
ADD COLUMN `login_user` varchar(20) DEFAULT NULL COMMENT '登录用户名称' AFTER `request_time`;