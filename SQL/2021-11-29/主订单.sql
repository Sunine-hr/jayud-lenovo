ALTER TABLE `order_info`
MODIFY COLUMN `status` int(11) NULL DEFAULT NULL COMMENT '订单状态:1正常,2草稿,3关闭,4待补全,5终止,6待取消处理,7待驳回处理,8待处理,9删除' AFTER `class_code`;