package com.jayud.common.constant;

/**
 * @anthor Satellite
 * Constant
 * 产量类
 * http://www.javalow.com
 * @date 2018-11-19-17:06
 **/
public abstract class Constant {

    public static final String PHONE_COLUMN = "su_phone";
    public static final String STATUS_COLUMN = "su_status";
    public static final String ID_COLUMN = "su_id";
    public static final String ROLE_ID_COLUMN = "role_id";
    public static final String ENQUIRY_ID_COLUMN = "enquiry_id";
    public static final String PARENT_ID_COLUMN = "parent_id";
    public static final String RESOURCE_ID_COLUMN = "resource_id";
    public static final String USER_ID_COLUMN = "user_id";
    public static final String IS_ENABLE_COLUMN = "is_enable";
    public static final String IS_DISPLAY_COLUMN = "is_display";
    public static final String SORT_COLUMN = "sort";
    public static final String NAME_COLUMN = "name";
    public static final String TYPE_COLUMN="type";
    public static final String WEIGHT_COLUMN="weight";
    public static final String SUCCESS = "success";

    public static final String SEND_MSG = "sendMsg";
    /**
     * 表示生成批次号模块名称,10000表示兴业票据的订单信息
     */
    public static final Integer TICKET_ID_GENERAL = 10000;
    /**
     * 表示生成批次号模块名称,10000表示兴业票据的订单信息
     */
    public static final Integer CONTRACT_ID_GENERAL = 20000;
    /**
     * 默认插入数据库版本号
     */
    public static final int VERSION = 1;

    /**
     * 发送手机验证码的间隔时间
     */
    public static final long SEND_VERIFYCODE_INTERVAL = 59;

    /**
     * 短信发送成功之后的有效的时间
     */
    public static final long VERIFYCODE_VALID_TIME = 300;


}
