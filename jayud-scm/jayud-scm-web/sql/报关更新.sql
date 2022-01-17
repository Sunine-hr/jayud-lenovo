--增加推送云报关字段
ALTER TABLE `jyd_scm`.`hg_bill`
DROP COLUMN `customs_state`,
DROP COLUMN `uid`,
MODIFY COLUMN `declare_state` int(0) NULL DEFAULT 0 COMMENT '报关单申报状态：0未提交，1已提交，2,生成完成，3提交成功，4报关完成' AFTER `hk_bill_no`,
ADD COLUMN `customs_state` int(0) NULL DEFAULT 0 COMMENT '报关单申报状态：0未提交，1已提交，2已完成' AFTER `is_confirm`,
ADD COLUMN `uid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_cs_0900_ai_ci NULL DEFAULT NULL COMMENT '云报关单id' AFTER `customs_state`;