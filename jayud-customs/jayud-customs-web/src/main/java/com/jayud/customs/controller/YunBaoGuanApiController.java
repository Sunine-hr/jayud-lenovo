package com.jayud.customs.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.RsaEncryptUtil;
import com.jayud.customs.feign.FileClient;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@Api(tags = "customs对接云报关接口")
@Slf4j
public class YunBaoGuanApiController {

    @Autowired
    IOrderCustomsService orderCustomsService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private FileClient fileClient;


//    @Value("${yunbaoguan.default.username}")
//    String defaultUsername;
//
//    @Value("${yunbaoguan.default.password}")
//    String defaultPassword;
//
//    @Value("${yunbaoguan.urls.base}")
//    String urlBase;
//
//    @Value("${yunbaoguan.urls.token}")
//    String urlToken;
//
//    @Value("${yunbaoguan.public-key}")
//    String publicKey;

    private final String YBG_TICKET_STR = "YBG_TICKET";

    /**
     * 根据云报关状态，更新oms系统报关单状态
     */
    public void UpdateBGProcess(){
        //获取报关打单及其之后状态的所有订单
        List<String> statuses = new ArrayList<>();
        statuses.add(OrderStatusEnum.CUSTOMS_C_3.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_4.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_5.getCode());
        statuses.add(OrderStatusEnum.CUSTOMS_C_6.getCode());
        List<OrderCustoms> orderCustoms = orderCustomsService.getOrderCustomsByStatus(statuses);

    }

    /**
     * 获取云报关的数据，根据委托号
     */



//    /**
//     * 向云报关发送API请求
//     *
//     * @param form
//     * @param url
//     * @return
//     */
//    private Map<String, Object> post(Object form, String url) {
//        Gson gson = new Gson();
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<MultiValueMap<String, String>> body = null;
////        body=new HttpEntity<MultiValueMap<String, String>>(JSONUtil.toBean(form,MultiValueMap.class),headers);
//
//        String data = gson.toJson(form);
//        Map<String, Object> map = this.doPost(data, url);
//        if (map == null) {
//            //没有返回重新调用一次
//            map = doPost(data, url);
//            if (map == null) {
//                log.warn("调用失败");
//                return new HashMap<>();
//            }
//        }
//        //token过期,重新请求
//        if (map.get("Message") != null && map.get("Message").toString().contains("已拒绝为此请求授权")) {
//            redisUtils.delete(YBG_TICKET_STR);
//            map = this.doPost(data, url);
//        }
//
//        return map;
//    }


//    private Map<String, Object> doPost(String form, String url) {
//        log.info("云报关参数:" + form);
//        String ticket = getTicket(null, null);
//
//        HttpResponse response = HttpRequest.post(url)
//                .header("X-Ticket", ticket)
//                .header(Header.CONTENT_TYPE.name(), "multipart/json")
//                .form("transfer_data", form)
//                .execute();
//        String feedback = response.body();
//
//        if (StringUtils.isEmpty(feedback)) {
//            return null;
//        }
////        log.info("报文:" + response.toString());
//        log.info("请求token信息:" + ticket);
//        log.info("vivo返回参数:" + feedback);
//        return JSONUtil.toBean(feedback, Map.class);
//    }

//    private Map<String, Object> postWithFile(Object form, MultipartFile file, String url) {
//        Gson gson = new Gson();
//        String data = gson.toJson(form);
//        log.info("参数:" + data);
//        File fw = new File(file.getOriginalFilename());
//        try {
//            FileUtils.copyInputStreamToFile(file.getInputStream(), fw);
//        } catch (IOException e) {
//            log.warn("文件流操作失败");
//        }
//        log.info("文件大小====={}=======名称{}", FileUtils.sizeOf(fw), fw.getName());
//        Map<String, Object> map = this.doPostWithFile(data, fw, url);
//        if (map == null) {
//            //没有返回重新调用一次
//            map = doPostWithFile(data, fw, url);
//            if (map == null) {
//                log.warn("请联系云报关客户");
//                return new HashMap<>();
//            }
//        }
//        //token过期,重新请求
//        if (map.get("Message") != null && map.get("Message").toString().contains("已拒绝为此请求授权")) {
//            redisUtils.delete(YBG_TICKET_STR);
//            map = this.doPostWithFile(data, fw, url);
//        }
//        return map;
//    }

//    private Map<String, Object> doPostWithFile(String form, File fw, String url) {
//        String feedback = HttpRequest.post(url)
//                .header("Authorization", getTicket(null, null))
//                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
//                .form("transfer_data", form)
//                .form("MultipartFile", fw)
//                .execute()
//                .body();
//        if (StringUtils.isEmpty(feedback)) {
//            return null;
//        }
//        log.info("响应参数:" + feedback);
//        if (fw.exists()) {
//            fw.delete();
//        }
//        return JSONUtil.toBean(feedback, Map.class);
//    }

//    /**
//     * 获取发送请求时必须的token
//     *
//     * @param userName
//     * @param password
//     * @return
//     */
//    private String getTicket(String userName, String password) {
//        String yunBaoGuanTicket = redisUtils.get(YBG_TICKET_STR);
//        if (yunBaoGuanTicket != null) {
//            return yunBaoGuanTicket;
//        }
//
//        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) ) {
//            //只要有一个参数为空，即调用默认的登录设置
//            userName = defaultUsername;
//            password = defaultPassword;
//            log.info("尝试使用配置文件中的默认配置进行云报关接口授权...");
//            String encryptedPassword = null;
//            try {
//                encryptedPassword = RsaEncryptUtil.getEncryptedPassword(password, publicKey);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (StringUtils.isBlank(encryptedPassword)) {
//                Asserts.fail(ResultEnum.UNAUTHORIZED, "云报关 RSA加密失败");
//            }
//
//
//            String feedback = HttpRequest.post(urlBase + urlToken)
//                    .header(Header.CONTENT_TYPE.name(), "application/json")
//                    .form("grant_type", "password")
//                    .form("username", userName)
//                    .form("password", encryptedPassword)
//                    .execute().body();
//            Map resultMap = JSONUtil.toBean(feedback, Map.class);
//            String access_token = MapUtil.getStr(resultMap, "access_token");
//            if (!StringUtils.isEmpty(access_token)) {
//                redisUtils.set(YBG_TICKET_STR, String.format("bearer %s", access_token), 82800);
//                return String.format("bearer %s", access_token);
//            }
//
//            Asserts.fail(ResultEnum.UNAUTHORIZED, "云报关 授权失败");
//        }
//        return null;
//    }
//
//
//    private Boolean check4Success(String feedback) {
//        Map<String, Object> map = JSONUtil.toBean(feedback, Map.class);
//
//        Integer status = MapUtil.getInt(map, "status");
//        String message = MapUtil.getStr(map, "message");
//
//        if (Objects.isNull(status) || Objects.isNull(message)) {
//            Asserts.fail(ResultEnum.PARAM_ERROR, "接口请求失败");
//        }
//        if (status == 0) {
//            return false;
//        }
//        return true;
//    }


}









    



