

-- 创建字段   第三方订单编号
ALTER TABLE order_inland_transport ADD COLUMN third_party_order_no varchar(200) DEFAULT NULL COMMENT '第三方订单编号' AFTER `main_order_no`;


-- 内陆表  创建系统类型版本
ALTER TABLE order_inland_transport ADD COLUMN create_user_type int(11) DEFAULT '0' COMMENT '创建人的类型(0:本系统,2:scm)' AFTER `update_time`;


-- 内陆表  标识  
ALTER TABLE order_inland_transport ADD COLUMN  type int(11) DEFAULT '0' COMMENT '外部调用区分标识(0:本系统,1:类型1 ,2:类型2)' AFTER `create_user_type`;

