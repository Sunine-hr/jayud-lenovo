-- 是否发送邮件
ALTER TABLE `order_customs`
    ADD COLUMN `is_send_mail` char(1)  NOT NULL DEFAULT '0' COMMENT '是否发送邮件(0-否 1-是)' AFTER `supervision_mode`;

INSERT INTO dict_type (name, code, create_user, create_time, status)
VALUES ('报关邮件收件人', 'BGEmail', 'admin', now(), '1');

-- 多个邮箱配置请用英文逗号隔开(aa@163.com,bb@163.com)
INSERT INTO dict (value, code, status, dict_type_code, create_user, create_time, remarks)
VALUES ('hg@jayud.com,luxun.li@jayud.com', 'LAND_TRANSPORT', '1', 'BGEmail', 'admin', now(), '陆路运输');
INSERT INTO dict (value, code, status, dict_type_code, create_user, create_time, remarks)
VALUES ('di@jayud.com,luxin.li@jayud.com,yt@jayud.com', 'AIR_TRANSPORT', '1', 'BGEmail', 'admin', now(), '空运');
INSERT INTO dict (value, code, status, dict_type_code, create_user, create_time, remarks)
VALUES ('di@jayud.com,luxin.li@jayud.com,yt@jayud.com', 'SEA_TRANSPORT', '1', 'BGEmail', 'admin', now(), '海运');
INSERT INTO dict (value, code, status, dict_type_code, create_user, create_time, remarks)
VALUES ('di@jayud.com,luxin.li@jayud.com,yt@jayud.com', 'EXPRESS', '1', 'BGEmail', 'admin', now(), '快递');
INSERT INTO dict (value, code, status, dict_type_code, create_user, create_time, remarks)
VALUES ('hg@jayud.com,luxun.li@jayud.com', 'INLAND', '1', 'BGEmail', 'admin', now(), '内陆');