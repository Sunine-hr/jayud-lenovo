package com.jayud.common.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LX
 * @ClassName: SendMsgUtil
 * @Description: TODO(发送短信工具类)
 * @date 2017年5月18日 下午3:57:57
 */
public class SendMsgUtil {

    private static final String SUCCESS = "2";

    /**
     * 发送手机短信息
     *
     * @param phone   手机号
     * @param content 短信内容
     * @return 000：成功 999：失败
     */
    public static int sendTextMessage(String phone, String content) {
        // 要验证的字符串
        // String str = "尊敬的客户，您本次短信的验证码为 [4568] ，5分钟内有效，如已经失效，请重新点击获取验证码。本消息为系统自动下发信息，请勿回复。";
        // 验证规则
        String regEx = "尊敬的客户，您本次短信的验证码为 (\\[*)(\\d+)(\\]*) ，5分钟内有效，如已经失效，请重新点击获取验证码。本消息为系统自动下发信息，请勿回复。";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        //新的短信接口
        if (rs) {
            return SendMsgUtil.sendMessage(phone, content);
        }
        return 0;
    }



    public static int sendMessage(String phone, String content) {
        String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);
        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");
        NameValuePair[] data = new NameValuePair[]{
                new NameValuePair("account", "C21259776"),
                new NameValuePair("password", "dc62c27bdd2294371e896d4b9dbca238"),
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content)};
        method.setRequestBody(data);

        try {
            client.executeMethod(method);
            String SubmitResult = method.getResponseBodyAsString();
            org.dom4j.Document doc = DocumentHelper.parseText(SubmitResult);
            org.dom4j.Element root = doc.getRootElement();
            String code = root.elementText("code");
            if (SUCCESS.equals(code)) {
                return HttpStatus.SC_OK;
            } else {
                return HttpStatus.SC_INTERNAL_SERVER_ERROR;
            }
        } catch (IOException ioE) {
            ioE.printStackTrace();
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } catch (DocumentException docE) {
            docE.printStackTrace();
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }

}
