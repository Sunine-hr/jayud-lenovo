package com.jayud.airfreight.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jayud.airfreight.feign.OauthClient;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.ForwarderBookingConfirmedFeedbackForm;
import com.jayud.airfreight.model.bo.ForwarderLadingFileForm;
import com.jayud.airfreight.model.bo.ForwarderLadingInfoForm;
import com.jayud.airfreight.model.bo.ForwarderVehicleInfoForm;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.VivoApiResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.exception.VivoApiException;
import com.jayud.common.utils.RsaEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * vivo数据接口服务
 *
 * @author william
 * @description
 * @Date: 2020-09-14 13:48
 */
@Service
@Slf4j
public class VivoServiceImpl implements VivoService {
    @Value("${vivo.default.username}")
    String defaultUsername;
    @Value("${vivo.default.password}")
    String defaultPassword;
    @Value("${vivo.default.scope}")
    String defaultScope;

    @Value("${vivo.urls.base}")
    String urlBase;

    @Value("${vivo.urls.token}")
    String urlToken;
    @Value("${vivo.urls.booking-confirm}")
    String urlBookingConfirm;
    @Value("${vivo.urls.vehicle-info}")
    String urlVehicleInfo;
    @Value("${vivo.urls.lading-file}")
    String urlLadingFile;
    @Value("${vivo.urls.lading-info}")
    String urlLadingInfo;


    @Value("${vivo.public-key}")
    String publicKey;

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OauthClient oauthClient;


    /**
     * 向vivo发送确认订舱的数据
     *
     * @param form
     * @return
     */
    @Override
    public Boolean forwarderBookingConfirmedFeedback(ForwarderBookingConfirmedFeedbackForm form) {
        String url = urlBase + urlBookingConfirm;
        return doPost(form, url);
    }

    /**
     * 货代车辆信息
     *
     * @param form
     * @return
     */
    @Override
    public Boolean forwarderVehicleInfo(ForwarderVehicleInfoForm form) {
        String url = urlBase + urlVehicleInfo;
        return doPost(form, url);
    }

    /**
     * 提单文件信息
     *
     * @param form
     * @param file
     * @return
     */
    @Override
    public Boolean forwarderLadingFile(ForwarderLadingFileForm form, MultipartFile file) {
        String url = urlBase + urlLadingFile;
        return doPostWithFile(form, file, url);
    }

    @Override
    public boolean forwarderLadingInfo(ForwarderLadingInfoForm form) {
        String url = urlBase + urlLadingInfo;
        return doPost(form, url);
    }


    /**
     * 向联想发送API请求
     *
     * @param form
     * @param url
     * @return
     */
    private Boolean doPost(Object form, String url) {
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> body = null;
//        body=new HttpEntity<MultiValueMap<String, String>>(JSONUtil.toBean(form,MultiValueMap.class),headers);

        log.info("vivo参数==========" + gson.toJson(form));
        String feedback = HttpRequest.post(url)
                .header("Authorization", String.format(getToken(null, null, null)))
                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                .form("transfer_data", gson.toJson(form))
                .execute()
                .body();

        return check4Success(feedback);
    }

    private Boolean doPostWithFile(Object form, MultipartFile file, String url) {
        Gson gson = new Gson();
        log.info("参数========" + gson.toJson(form));
        File fw = new File(file.getOriginalFilename());
        String feedback = "";
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), fw);
            log.info("文件大小====={}=======名称{}", FileUtils.sizeOf(fw), fw.getName());
            feedback = HttpRequest.post(url)
                    .header("Authorization", String.format(getToken(null, null, null)))
                    .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                    .form("transfer_data", gson.toJson(form))
                    .form("MultipartFile", fw)
                    .execute()
                    .body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check4Success(feedback);
    }

    /**
     * 获取发送请求时必须的token
     *
     * @param userName
     * @param password
     * @param scope
     * @return
     */
    private String getToken(String userName, String password, String scope) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(scope)) {
            //只要有一个参数为空，即调用默认的登录设置
            userName = defaultUsername;
            password = defaultPassword;
            scope = defaultScope;
            log.info("尝试使用配置文件中的默认配置进行vivo接口授权...");
            String encryptedPassword = null;
            try {
                encryptedPassword = RsaEncryptUtil.getEncryptedPassword(password, publicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(encryptedPassword)) {
                Asserts.fail(ResultEnum.UNAUTHORIZED, "VIVO RSA加密失败");
            }


            String feedback = HttpRequest.post(urlBase + urlToken)
                    .header(Header.CONTENT_TYPE.name(), "application/x-www-form-urlencoded")
                    .form("grant_type", "password")
                    .form("username", userName)
                    .form("password", encryptedPassword)
                    .form("scope", scope
                    ).execute().body();
            Map resultMap = JSONUtil.toBean(feedback, Map.class);
            String access_token = MapUtil.getStr(resultMap, "access_token");
            if (!StringUtils.isEmpty(access_token)) {
                return String.format("Bearer %s", access_token);
            }
            Asserts.fail(ResultEnum.UNAUTHORIZED, "vivo 授权失败");
        }
        return null;
    }

    private Boolean check4Success(String feedback) {
        Map<String, Object> map = JSONUtil.toBean(feedback, Map.class);

        Integer status = MapUtil.getInt(map, "status");
        String message = MapUtil.getStr(map, "message");

        if (Objects.isNull(status) || Objects.isNull(message)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "接口请求失败");
        }
        if (status == 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据登录用户查询客户名称
     */
    @Override
    public JSONObject getCustomerInfoByLoginUserName() {
        //查询客户id
        ApiResult result = this.oauthClient.getSystemUserByName(UserOperator.getToken());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询用户信息失败 message={}", result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject systemUser = JSONUtil.parseObj(result.getData());
        Long companyId = systemUser.getLong("companyId");

        result = omsClient.getCustomerInfoById(companyId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message={}", result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject customerInfo = JSONUtil.parseObj(result.getData());
        return customerInfo;
    }
}
