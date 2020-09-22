package com.jayud.common.constant;

/**
 * 短信模板编码 常量类
 *
 * @author: xiaoy
 * @Date: 2019/11/22
 * @Time: 14:17
 */
public abstract class SmsTemplateConstant {

    /**
     * 注册验证码
     */
    public static final Integer REGISTER_VERIFY_CODE = 1001;

    /**
     * 修改验证码
     */
    public static final Integer UPDATE_VERIFY_CODE = 1002;

    public static final String TIME="5分钟";

    public static final String UPDATE_STR = "修改";

    public static final String FORGET_STR = "忘记";

    public static final String PASSWORD_STR = "密码";

    public static final String PHONE_BIND_STR = "手机绑定";

    /**
     * 注册27天未完善资料提醒
     */
    public static final Integer REGISTER_REMIND_27 = 2001;

    /**
     * 注册30天未完善资料提醒
     */
    public static final Integer REGISTER_REMIND_30 = 2002;

    /**
     * 资料审核通过
     */
    public static final Integer DATA_AUDIT_PASS = 3001;

    /**
     * 资料审核驳回
     */
    public static final Integer DATA_AUDIT_REJECT = 3002;

    /**
     * 票据发布 审核成功
     */
    public static final Integer TICKET_PUBLISH_AUDIT_PASS = 4001;

    /**
     * 票据发布 审核驳回
     */
    public static final Integer TICKET_PUBLISH_AUDIT_REJECT = 4002;

    /**
     * 企业信用贷 放款完成
     */
    public static final Integer CREDIT_COMPLETE = 5001;
}
