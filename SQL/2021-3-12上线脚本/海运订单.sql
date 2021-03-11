--海运订单表添加中转港字段
ALTER TABLE `sea_order`
ADD COLUMN `transit_port_code` varchar(20) NULL COMMENT '中转港' ;

--海运订船表添加截关时间字段
ALTER TABLE `sea_bookship`
ADD COLUMN `closing_time` datetime COMMENT '截关时间' ;

--goods表label字段扩充到200
ALTER TABLE `jayud_oms`.`goods`
MODIFY COLUMN `label` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签（空运是唛头）' AFTER `order_no`