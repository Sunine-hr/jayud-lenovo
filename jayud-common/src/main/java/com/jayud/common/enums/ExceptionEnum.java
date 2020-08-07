package com.jayud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * @author hanbin
 * @version 1.0.0
 * @ClassName ExceptionEnum.java
 * @Description TODO 请求返回体的错误码和错误信息枚举
 * @createTime 2019年11月21日 11:47:00
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnum {
    /**
     * 企业已经认证
     */
    ALREADY_AUTH(10001, "企业已经认证"),

    /**
     * 企业信息不存在
     */
    ENTERPRISE_NOT_EXIST(10002, "企业用户信息不存在"),
    /**
     * 企业信息查询失败
     */
    ENTERPRISE_QUERY_EXIST(10003, "企业信息查询失败!"),
    /**
     * 调用兴业查询获取打款码失败
     */
    ZJR_PAY_FAIL(10004, "查询获取打款码失败!"),

    /***
     *开户失败
     */
    OPEN_ACCOUNT_FAIL(10005, "企业开户失败"),

    /**
     * 多账户添加失败
     */
    MANY_ACCOUNT_FAIL(10006, "多账户添加失败"),

    /**
     * 多账户添加失败
     */
    CLOSE_ACCOUNT(10007, "账户关闭"),

    /**
     * 票据资料保存失败
     */
    TICKET_SAVE_FAIL(10008, "票据资料保存失败"),
    /**
     * 票据已经发布过
     */
    TICKET_ALREADY_PUSH(10009, "该票号已经发布过"),

    /**
     * 票据编号不合法
     */
    TICKET_NUM_INVALID(10010, "票据编号不合法"),
    /**
     * 银行账号状态不正确
     */
    BANK_STATUS_FAIL(10011, "账户关闭"),
    /**
     * * 支付接口调用失败
     */
    PAY_INTEGER_FAIL(10012, "支付接口支付失败"),
    /**
     * 甲方信息不存在
     */
    CONTRACT_MY_EXIST(10013, "签署合同甲方信息不存在"),

    /**
     * 签署合同买方信息不存在
     */
    CONTRACT_BUY_EXIST(10014, "签署合同已方信息不存在"),

    /**
     * 买方收票银行账号不存在
     */
    BUY_BANK_EXIST(10015, "买方收票银行账户不存在!"),

    /**
     * 倚天鉴账户开户失败
     */
    YTJ_USER_FAIL(10016, "执剑人账户开户失败!"),
    /**
     * 银行账户不存存
     */
    ACCOUNT_NOT_EXIT(10017, "银行账户不存在!"),
    /**
     * 卖方票据列表为空
     */
    SELL_LIST_EMPTY(10018, "卖方票据列表为空!"),
    /**
     * 卖方收款账户不存在
     */
    SELL_ACCOUNT_EMPTY(10019, "卖方收款账户不存在!"),
    /**
     * 票据超期
     */
    TICKET_OVER_TIME(10020, "票据超期!"),
    /**
     * 下架失败,该票据不能被下架
     */
    TICKET_LOWER(10021, "下架失败,该票据不能被下架!"),

    /**
     * 买方已经支付
     */
    PAY_SUCCESS(10022, "该票据订单已支付,请勿重新支付!"),

    /**
     * 订单已经创建
     */
    ORDER_CREATE(10023, "该订单已创建,请勿重新创建!"),
    /**
     * 没有查到该票据信息
     */
    TICKET_NO_EXIT(10024, "没有查到该票据信息"),
    /**
     * 卖方支行信息不存在
     */
    BRANCH_NO_EXIT(10025, "卖方银行账户支行信息不能为空"),
    /**
     * 买方支行信息不存在
     */
    BUY_BRANCH_NO_EXIT(10026, "买方银行账户支行信息不能为空"),
    /**
     * 支付账户余额不足
     */
    AMT_NO_PAY(10027, "支付账户余额不足"),
    /**
     * 该票据已支付
     */
    TICKET_ALREADY_PAY(10028, "该票据已支付"),

    /**
     * 支付密码不正确
     */
    PAY_IS_ERROR(10029, "支付密码不正确"),
    /**
     * 票据超期
     */
    COMMIT_REPAIRY(10030, "请勿多次支付!");

    private Integer code;
    private String msg;
}
