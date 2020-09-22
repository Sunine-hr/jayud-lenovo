package com.jayud.common.utils;

import java.io.Serializable;
import java.util.Date;

/**
 * 包装验证码,手机号码,发送时间
 * <p>
 * 用于发送验证码的时候将这三个属性保存到session当中去,但是分别保存的时候不方便所以使用一个vo来进行封装
 *
 * @author hb
 */
public class VerifyCode implements Serializable {

    public VerifyCode(String code, String phoneNumber, Date sendTime) {

        this.code = code;
        this.phoneNumber = phoneNumber;
        this.sendTime = sendTime;
    }

    private String code;
    private String phoneNumber;
    private Date sendTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
