ALTER TABLE `jimu_report`
MODIFY COLUMN `view_count` bigint(15) NULL DEFAULT 0 COMMENT '浏览次数' AFTER `template`;
ALTER TABLE `jimu_report`
MODIFY COLUMN `json_str` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'json字符串' AFTER `type`;
ALTER TABLE `jimu_report_link`
ADD COLUMN `expression` varchar(255) NULL COMMENT '表达式' AFTER `link_chart_id`;
-- ALTER TABLE `jimu_report_db_field`
-- ADD COLUMN `search_format` varchar(50) NULL COMMENT '查询时间格式化表达式' AFTER `search_value`;

ALTER TABLE `jimu_report_db_param`
ADD COLUMN `search_format` varchar(50) NULL COMMENT '查询时间格式化表达式' AFTER `dict_code`;
UPDATE jimu_report SET json_str=replace(json_str,'"subtotal":"totalField"','"funcname":"SUM"');
-- ALTER TABLE `jimu_report`
-- ADD COLUMN `css_str` text NULL COMMENT 'css增强' AFTER `view_count`,
-- ADD COLUMN `js_str` text NULL COMMENT 'js增强' AFTER `css_str`;
ALTER TABLE `jimu_report_link`
CHANGE COLUMN `expression` `requirement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件' AFTER `link_chart_id`;
-- ALTER TABLE `jimu_report_db_field`
-- ADD COLUMN `ext_json` text  NULL COMMENT '参数配置' AFTER `search_format`;
ALTER TABLE `jimu_report_db_param`
ADD COLUMN `ext_json`  text  NULL COMMENT '参数配置' AFTER `search_format`;
ALTER TABLE `jimu_report_db`
MODIFY COLUMN `is_list` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是列表0否1是 默认0' AFTER `api_method`;