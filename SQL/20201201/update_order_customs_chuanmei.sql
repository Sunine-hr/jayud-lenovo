--20201202 财务功能-报关表
ALTER TABLE order_customs ADD COLUMN yun_customs_no varchar(100) DEFAULT NULL COMMENT '报关单号' AFTER need_input_cost;