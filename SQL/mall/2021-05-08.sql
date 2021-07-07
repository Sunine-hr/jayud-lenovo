SET FOREIGN_KEY_CHECKS=0;
ALTER TABLE `quotation_template` ADD COLUMN `data_type`  int(10) NULL DEFAULT NULL COMMENT '数据类型(1报价模板 2报价管理)' AFTER `volume_unit`;
SET FOREIGN_KEY_CHECKS=1;

update quotation_template q
set data_type=1
where not EXISTS(select 1 from offer_info where qie = q.id);

update quotation_template q
set data_type=2
where EXISTS(select 1 from offer_info where qie = q.id);