--添加附件路径file_path和附件名字file_name字段
alter table order_address add column file_path varchar(500) comment '附件路径(多个逗号隔开)';
alter table order_address add column file_name varchar(500) comment '附件名字(多个逗号隔开)';
