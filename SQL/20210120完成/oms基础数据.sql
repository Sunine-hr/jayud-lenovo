
-- 2021-1-26 李达荣：费用类型增加税率
ALTER TABLE `cost_genre`
ADD COLUMN `tax_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '税率' AFTER `name`;

ALTER TABLE `cost_genre` 
MODIFY COLUMN `tax_rate` decimal(10, 2) NULL DEFAULT 0 COMMENT '税率' AFTER `name`;

-- 2021-1-26 李达荣：业务类型税率字段删掉
ALTER TABLE `product_biz`
DROP COLUMN `tax_rate`;