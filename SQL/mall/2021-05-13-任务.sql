SET
FOREIGN_KEY_CHECKS=0;

ALTER TABLE `jayud_shop`.`bill_task` MODIFY COLUMN `remarks` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流轨迹通知描述' AFTER `score`;

ALTER TABLE `jayud_shop`.`bill_task_relevance` MODIFY COLUMN `remarks` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流轨迹通知描述' AFTER `score`;

ALTER TABLE `jayud_shop`.`bill_task_relevance` MODIFY COLUMN `status` char (1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0未激活 1已激活,未完成 2已完成)' AFTER `remarks`;

ALTER TABLE `jayud_shop`.`customer_goods`
    ADD COLUMN `brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌' AFTER `remark`;

ALTER TABLE `jayud_shop`.`quotation_template` MODIFY COLUMN `arrive_warehouse` varchar (1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可达仓库(fab_warehouse.id),多个用逗号分隔' AFTER `destination_port`;

ALTER TABLE `jayud_shop`.`quotation_template` MODIFY COLUMN `visible_uid` varchar (1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可见客户(customer.id，多客户时逗号分隔用户ID)' AFTER `visible_customer`;

ALTER TABLE `jayud_shop`.`quotation_template` MODIFY COLUMN `gid` varchar (1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '货物类型(goods_type id),多个用逗号分隔' AFTER `gidtype`;

ALTER TABLE `jayud_shop`.`quotation_template` MODIFY COLUMN `area_id` varchar (1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '集货仓库(shipping_area id),多个都号分隔' AFTER `gid`;

ALTER TABLE `jayud_shop`.`quotation_template` MODIFY COLUMN `qid` varchar (1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报价类型(quotation_type id),多个用逗号分隔' AFTER `qidtype`;

ALTER TABLE `jayud_shop`.`task`
    ADD COLUMN `types` int(10) NULL DEFAULT NULL COMMENT '类型(1提单任务 2运单任务)' AFTER `create_time`;

CREATE TABLE `jayud_shop`.`task_execution_rule`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `from_task_type` int(10) NULL DEFAULT NULL COMMENT 'from任务类型(task types)',
    `from_task_id`   bigint(20) NULL DEFAULT NULL COMMENT 'from任务id(task id)',
    `from_task_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'from任务代码(task task_code)',
    `from_task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'from任务名称(task task_name)',
    `to_task_type`   int(10) NULL DEFAULT NULL COMMENT 'to任务类型(task types)',
    `to_task_id`     bigint(20) NULL DEFAULT NULL COMMENT 'to任务id(task id)',
    `to_task_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'to任务代码(task task_code)',
    `to_task_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'to任务名称(task task_name)',
    `status`         char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0无效 1有效)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务执行规则表(from where to where)' ROW_FORMAT = Dynamic;

ALTER TABLE `jayud_shop`.`waybill_task` MODIFY COLUMN `remarks` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流轨迹通知描述' AFTER `score`;

ALTER TABLE `jayud_shop`.`waybill_task_relevance` COMMENT = '运单(订单)任务关联';

ALTER TABLE `jayud_shop`.`waybill_task_relevance` MODIFY COLUMN `remarks` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流轨迹通知描述' AFTER `score`;

ALTER TABLE `jayud_shop`.`waybill_task_relevance` MODIFY COLUMN `status` char (1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态(0未激活 1已激活,未完成 2已完成)' AFTER `remarks`;

SET
FOREIGN_KEY_CHECKS=1;