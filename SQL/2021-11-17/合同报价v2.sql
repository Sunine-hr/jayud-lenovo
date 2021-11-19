ALTER TABLE `contract_quotation`
CHANGE COLUMN `audit_status` `opt_status` int(5) NULL DEFAULT 2 COMMENT '流程状态(1:未提交,2:待部门经理审核,3:待公司法务审核,4:待总审核,5:未通过,6:待完善,7:已完成)' AFTER `end_time`,
MODIFY COLUMN `customer_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户/供应商code' AFTER `number`,
ADD COLUMN `type` int(5) NULL COMMENT '合同对象(1:客户,2:供应商)' AFTER `remarks`,
ADD COLUMN `legal_entity_id` bigint(20) NULL COMMENT '接单法人id' AFTER `type`,
ADD COLUMN `reasons_failure` varchar(255) NULL COMMENT '未通过原因' AFTER `legal_entity_id`;


ALTER TABLE `contract_quotation`
MODIFY COLUMN `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报价编号(合同报价编号)' AFTER `id`,
ADD COLUMN `file` varchar(255) NULL COMMENT '合同报价附件' AFTER `reasons_failure`,
ADD COLUMN `file_name` varchar(255) NULL COMMENT '合同报价附件名称' AFTER `file`,
ADD COLUMN `sign_contract_file` varchar(255) NULL COMMENT '签署合同附件' AFTER `file_name`,
ADD COLUMN `sign_contract_file_name` varchar(255) NULL COMMENT '签署合同附件名称' AFTER `sign_contract_file`,
ADD COLUMN `sign_offer_file` varchar(255) NULL COMMENT '签署报价附件' AFTER `sign_contract_file_name`,
ADD COLUMN `sign_offer_file_name` varchar(255) NULL COMMENT '签署报价附件名称' AFTER `sign_offer_file`;


ALTER TABLE `contract_quotation`
ADD COLUMN `legal_audit` varchar(255) NULL COMMENT '法务审核' AFTER `sign_offer_file_name`,
ADD COLUMN `dep_manager_review` varchar(255) NULL COMMENT '部门经理审核' AFTER `legal_audit`,
ADD COLUMN `general_manager_review` varchar(255) NULL COMMENT '总经理审核' AFTER `dep_manager_review`;