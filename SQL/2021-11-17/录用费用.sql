ALTER TABLE `order_receivable_cost`
ADD COLUMN `create_type` int(10) NULL COMMENT '创建类型(1:其他,2:合同)' AFTER `unit_name`;