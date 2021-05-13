ALTER TABLE `order_customs`
MODIFY COLUMN `biz_model` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆运输)' AFTER `encode_pic_name`;