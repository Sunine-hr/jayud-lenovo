ALTER TABLE `supplier_info`
ADD COLUMN `gps_type` int(20) NULL DEFAULT NULL COMMENT 'gps厂商(1:云港通,2:北斗)' AFTER `gps_address`;


ALTER TABLE `supplier_info`
ADD COLUMN `gps_req_param` text NULL COMMENT 'gps请求参数(json格式)' AFTER `gps_type`;


CREATE TABLE `gps_positioning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(200) DEFAULT NULL COMMENT '订单编号',
  `plate_number` varchar(50) NOT NULL COMMENT '车牌号',
  `vehicle_status` varchar(50) DEFAULT NULL COMMENT '车辆状态',
  `direction` varchar(50) DEFAULT NULL COMMENT '车辆方向',
  `latitude` varchar(50) DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(50) DEFAULT NULL COMMENT '经度',
  `speed` varchar(50) DEFAULT NULL COMMENT '速度',
  `status` int(5) DEFAULT NULL COMMENT '状态(1:实时,2:历史轨迹)',
  `type` int(10) DEFAULT NULL COMMENT 'gps厂商(1:云港通,2:北斗)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结算时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `gps_positioning`
ADD COLUMN `gps_time` datetime NULL COMMENT 'gps定位时间' AFTER `end_time`;