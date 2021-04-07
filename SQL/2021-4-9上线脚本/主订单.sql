-- 主订单创建类型
ALTER TABLE `order_info`
ADD COLUMN `create_user_type` int(0) NULL DEFAULT 0 COMMENT '创建人的类型(0:本系统,1:vivo)' AFTER `reject_comment`;