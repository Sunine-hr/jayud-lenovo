--20201214 二期优化,优先级1
ALTER TABLE `order_info`
ADD COLUMN `encode` varchar(20) NULL COMMENT '外部报关放行的六联单号' AFTER `customs_release`;