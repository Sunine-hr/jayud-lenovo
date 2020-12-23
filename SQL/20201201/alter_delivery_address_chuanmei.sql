--20201214 二期优化,优先级1
ALTER TABLE `delivery_address`
MODIFY COLUMN `province` int(20) NULL COMMENT '省主键' AFTER `type`,
MODIFY COLUMN `city` int(20) NULL COMMENT '市主键' AFTER `province`;