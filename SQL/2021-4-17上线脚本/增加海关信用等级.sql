ALTER TABLE `customer_info`
ADD COLUMN `national_credit` int(5) NULL DEFAULT 1 COMMENT '国家征信(0:异常,1:非异常)' AFTER `updated_user`,
ADD COLUMN `customs_credit` int(5) NULL DEFAULT 1 COMMENT '海关征信(0:异常,1:非异常)' AFTER `national_credit`,
ADD COLUMN `customs_credit_rating` int(10) NULL COMMENT '海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)' AFTER `customs_credit`;

ALTER TABLE `supplier_info`
ADD COLUMN `national_credit` int(0) NULL DEFAULT 1 COMMENT '国家征信(0:异常,1:非异常)' AFTER `update_user`,
ADD COLUMN `customs_credit` int(0) NULL DEFAULT 1 COMMENT '海关征信(0:异常,1:非异常)' AFTER `national_credit`,
ADD COLUMN `customs_credit_rating` int(0) NULL DEFAULT NULL COMMENT '海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)' AFTER `customs_credit`;