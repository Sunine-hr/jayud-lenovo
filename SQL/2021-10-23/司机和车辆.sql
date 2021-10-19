ALTER TABLE `driver_info`
ADD COLUMN `account_name` varchar(255) NULL COMMENT '账户名' AFTER `applet_id`,
ADD COLUMN `account` varchar(255) NULL COMMENT '账号' AFTER `account_name`,
ADD COLUMN `account_bank` varchar(255) NULL COMMENT '开户行' AFTER `account`,
ADD COLUMN `billing_cycle` int(10) NULL COMMENT '结算周期(1:当月,2:次月)' AFTER `account_bank`,
ADD COLUMN `account_status` int(10) NULL COMMENT '账户状态(0:禁用,1:启用)' AFTER `billing_cycle`,
ADD COLUMN `driver_license_num` varchar(50) NULL COMMENT '驾驶证号' AFTER `account_status`,
ADD COLUMN `driving_age` int(20) NULL COMMENT '驾龄' AFTER `driver_license_num`,
ADD COLUMN `date_collection` datetime NULL COMMENT '初次领证日期' AFTER `driving_age`,
ADD COLUMN `valid_period` datetime NULL COMMENT '有效期' AFTER `date_collection`,
ADD COLUMN `driver_license_file_num` varchar(50) NULL COMMENT '驾驶证档案号' AFTER `valid_period`,
ADD COLUMN `issuing_authority` varchar(255) NULL COMMENT '发证机关' AFTER `driver_license_file_num`,
ADD COLUMN `driver_license_img` varchar(500) NULL COMMENT '驾驶证图片路径' AFTER `issuing_authority`,
ADD COLUMN `driver_license_img_name` varchar(500) NULL COMMENT '驾驶证图片名称' AFTER `driver_license_img`,
ADD COLUMN `id_card_img` varchar(500) NULL COMMENT '身份证图片路径' AFTER `driver_license_img_name`,
ADD COLUMN `id_card_img_name` varchar(500) NULL COMMENT '身份证图片名称' AFTER `id_card_img`;



ALTER TABLE `vehicle_info`
ADD COLUMN `insurance_company` varchar(255) NULL COMMENT '保险公司' AFTER `type`,
ADD COLUMN `is_insured` tinyint(1) NULL COMMENT '是否保险' AFTER `insurance_company`,
ADD COLUMN `brand` varchar(255) NULL COMMENT '品牌' AFTER `is_insured`,
ADD COLUMN `frame_num` varchar(50) NULL COMMENT '车架号' AFTER `brand`,
ADD COLUMN `last_annual_review_date` datetime NULL COMMENT '最近一次年审日期' AFTER `frame_num`,
ADD COLUMN `next_annual_review_date` datetime NULL COMMENT '下次年审日期' AFTER `last_annual_review_date`,
ADD COLUMN `purchase_date` datetime NULL COMMENT '购置日期' AFTER `next_annual_review_date`,
ADD COLUMN `last_quarterly_review_date` datetime NULL COMMENT '最近一次季审日期' AFTER `purchase_date`,
ADD COLUMN `next_quarterly_review_date` datetime NULL COMMENT '下一次季审日期' AFTER `last_quarterly_review_date`,
ADD COLUMN `commercial_insurance_expiration` datetime NULL COMMENT '商业险到期日期' AFTER `next_quarterly_review_date`,
ADD COLUMN `compulsory_insurance_date_expiry` datetime NULL COMMENT '强制险到期日期' AFTER `commercial_insurance_expiration`;