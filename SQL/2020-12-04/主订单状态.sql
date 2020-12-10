
-- 2020年12月9日李达荣，功能描述：主订单更新状态描述
ALTER TABLE `jayud_oms`.`order_info`
MODIFY COLUMN `status` int(10) NULL DEFAULT NULL COMMENT '订单状态1正常 2草稿 3关闭 4待补全 5终止 6待处理' AFTER `class_code`;