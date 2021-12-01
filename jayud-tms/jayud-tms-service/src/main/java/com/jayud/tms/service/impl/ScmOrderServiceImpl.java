package com.jayud.tms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.RSAUtils;
import com.jayud.tms.feign.FileClient;
import com.jayud.tms.feign.MsgClient;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.ScmTransportationInformationForm;
import com.jayud.tms.service.IScmOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 供应链订单接口 服务类
 * </p>
 *
 * @author cyc
 * @since 2021-10-08
 */
@Service
@Slf4j
public class ScmOrderServiceImpl implements IScmOrderService {

    @Value("${scm.default.username:}")
    private String defaultUsername;
    @Value("${scm.default.password:}")
    private String defaultPassword;
    @Value("${scm.urls.base:}")
    private String urlBase;
    @Value("${scm.urls.token:}")
    private String urlToken;
    // 设置车次状态
    @Value("${scm.urls.set-train-number-status:}")
    private String setTrainNumberStatus;
    // 设置运输公司信息
    @Value("${scm.urls.accept-transportation-information:}")
    private String urlAcceptTransportationInformation;

    private final String SCM_TOKEN_STR = "SCM_TOKEN";

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private OmsClient omsClient;
    /**
     * 设置车次状态
     * @param trainStatus
     * @param truckNo
     * @return
     */
    @Override
    public String setManifest(String trainStatus, String truckNo,String unitCode) {
        Map<String, Object> form = new HashMap<>();
        form.put("trainStatus", trainStatus);
        form.put("truckNo", truckNo);
        form.put("userName", defaultUsername);


        //在根据子订单  客户 unit_code  查询到客户id
        ApiResult<Long> customerByCode = omsClient.getCustomerByCode(unitCode);
        if (customerByCode.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + customerByCode.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        ApiResult result = omsClient.findClientSecretOne(customerByCode.getData().toString());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result.getData());
        String s1 = JSONObject.toJSONString(form);
        String sjm = null;
        try {

            sjm = RSAUtils.privateEncrypt(s1, RSAUtils.getPrivateKey(jsonObject.getStr("appPrivateSecret")));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return doPost(sjm, urlBase + setTrainNumberStatus);
    }

    /**
     * 设置运输公司信息
     * @param scmTransportationInformationForm
     * @return
     */
    @Override
    public String acceptTransportationInformation(ScmTransportationInformationForm scmTransportationInformationForm, String unitCode) {
        scmTransportationInformationForm.setUserName(defaultUsername);

        //在根据子订单  客户 unit_code  查询到客户id
        ApiResult<Long> customerByCode = omsClient.getCustomerByCode(unitCode);
        if (customerByCode.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + customerByCode.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        ApiResult result = omsClient.findClientSecretOne(customerByCode.getData().toString());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        String sjm = null;
        try {
            sjm = RSAUtils.privateEncrypt(JSONObject.toJSONString(scmTransportationInformationForm), RSAUtils.getPrivateKey(jsonObject.getStr("appPrivateSecret")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return doPost(sjm, urlBase + urlAcceptTransportationInformation);
    }

    /**
     * 刷新Token
     */
    @Override
    public void refreshToken(){
        try{
            String token = login(null, null);
            log.info("刷新token信息:" + token);
        }catch(Exception e){
            e.printStackTrace();
            log.warn("请求token失败");
        }

    }

    /**
     * post请求
     * @param form
     * @param url
     * @return
     */
    private String doPost(String form, String url){
        log.info("请求供应链 url: {} 参数: {}", url, form);
        String token = login(null, null);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("data", form);
        String s = jsonObject.toString();

        HttpResponse response = HttpRequest.post(url)
//                .header("token", token)  // HttpHeaders.CONTENT_TYPE
                .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                .body(s)
                .execute();


//        HttpResponse response = cn.hutool.http.HttpRequest
//                .post(url)
////                .header("token", token)
//                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .body(s)
//                .execute();
        String feedback = response.body();
        System.out.println(""+feedback);
        cn.hutool.json.JSONObject parseObj = JSONUtil.parseObj(feedback);
        String data2 = parseObj.getStr("data");
        String publicKey = parseObj.getStr("publicKey");
        //解密
        String jmm = null;
        try {
            jmm = RSAUtils.publicDecrypt(data2, RSAUtils.getPublicKey(publicKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        cn.hutool.json.JSONObject paj = JSONUtil.parseObj(jmm);
        String code = paj.getStr("code");
        log.info("解密后的数据："+code);

        if (StringUtils.isEmpty(code)) {
            return null;
        }

        log.info("报文:" + response.toString());
        log.info("请求token信息:" + token);
        log.info("供应链返回参数:" + feedback);
        return code;
    }

    /**
     * 登录
     * 获取发送请求时必须的token
     *
     * @param userName
     * @param password
     * @return
     */
    private String login(String userName, String password) {
        String scmToken = redisUtils.get(SCM_TOKEN_STR);
        if (scmToken != null) {
            return scmToken;
        }

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) ) {
            //只要有一个参数为空，即调用默认的登录设置
            userName = defaultUsername;
            password = defaultPassword;
        }

        log.info("尝试使用配置文件中的默认配置进行供应链接口授权...");

        // 签名操作
        Map<String, Object> form = new HashMap<>();
        form.put("loginname", userName);
        form.put("password", password);
        form.put("bizData", MapUtil.of("oprTime", DateUtils.format(new Date(), "yyyyMMddHHmmss")));
        String jsonStr = JSONObject.toJSONString(form.get("bizData"));
        String encodeValue = DigestUtils.md5DigestAsHex((userName + jsonStr + password).getBytes());
        form.put("sign", encodeValue);

        String feedback = HttpRequest.post(urlBase + urlToken)
                .header(Header.CONTENT_TYPE.name(), "application/json")
                .body(JSONUtil.toJsonStr(form)
                ).execute().body();
        Map resultMap = JSONUtil.toBean(feedback, Map.class);
        String token = MapUtil.getStr(resultMap, "data");
        if (!StringUtils.isEmpty(token)) {
            redisUtils.set(SCM_TOKEN_STR, token, 28800);
            return token;
        }

        Asserts.fail(ResultEnum.UNAUTHORIZED, "供应链 授权失败");
        return null;
    }

}
