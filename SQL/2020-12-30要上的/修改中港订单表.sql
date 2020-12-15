-- 2020年12月15日李达荣，功能描述：中港订单增加创建类型
ALTER TABLE `order_transport`
ADD COLUMN `create_user_type` int(5) NULL DEFAULT 0 COMMENT '创建人的类型(0:本系统,1:vivo)' AFTER `created_user`;

-- 2020年12月15日李达荣，功能描述：中港订单增加第三方订单号
ALTER TABLE `order_transport`
ADD COLUMN `third_party_order_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方订单编号' AFTER `order_no`;