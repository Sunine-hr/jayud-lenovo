ALTER TABLE `jayud_shop`.`task`
    ADD COLUMN `types` int(10) NULL COMMENT '类型(1提单任务 2运单任务)' AFTER `create_time`;