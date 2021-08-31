ALTER TABLE `supplier_info`
ADD COLUMN `gps_type` int(20) NULL DEFAULT NULL COMMENT 'gps厂商(1:云港通,2:北斗)' AFTER `gps_address`;


ALTER TABLE `supplier_info`
ADD COLUMN `gps_req_param` text NULL COMMENT 'gps请求参数(json格式)' AFTER `gps_type`;