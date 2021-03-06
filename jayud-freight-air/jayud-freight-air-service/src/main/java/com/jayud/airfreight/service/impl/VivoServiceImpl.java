package com.jayud.airfreight.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jayud.airfreight.feign.*;
import com.jayud.airfreight.model.bo.*;
import com.jayud.airfreight.model.bo.vivo.*;
import com.jayud.airfreight.model.enums.VivoRejectionStatusEnum;
import com.jayud.airfreight.model.po.AirBooking;
import com.jayud.airfreight.model.po.AirExceptionFeedback;
import com.jayud.airfreight.model.po.AirExtensionField;
import com.jayud.airfreight.model.po.AirOrder;
import com.jayud.airfreight.model.vo.GoodsVO;
import com.jayud.airfreight.model.vo.OrderAddressVO;
import com.jayud.airfreight.service.IAirBookingService;
import com.jayud.airfreight.service.IAirExtensionFieldService;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.VivoService;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.*;
import com.jayud.common.exception.Asserts;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.exception.VivoApiException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileUtil;
import com.jayud.common.utils.RandomGUID;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * vivo??????????????????
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
    @Value("${vivo.urls.air-freight-info}")
    String urlAirFreightInfo;
    @Value("${vivo.urls.land-transportation-cost}")
    String urlLandTransportationCost;
    @Value("${vivo.urls.booking-rejected}")
    String urlBookingRejected;
    @Value("${vivo.urls.dispatch-rejected}")
    String urlDispatchRejected;

    @Value("${vivo.public-key}")
    String publicKey;

    private final String VIVO_TOEKN_STR = "VIVO_TOKEN";

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IAirExtensionFieldService airExtensionFieldService;
    @Autowired
    private IAirBookingService airBookingService;
    @Autowired
    private MsgClient msgClient;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private TmsClient tmsClient;
    @Autowired
    private IAirOrderService airOrderService;
    @Autowired
    private RedisUtils redisUtils;


    /**
     * ???vivo???????????????????????????
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> forwarderBookingConfirmedFeedback(ForwarderBookingConfirmedFeedbackForm form) {
        String url = urlBase + urlBookingConfirm;
        return post(form, url);
    }

    /**
     * ??????????????????
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> forwarderVehicleInfo(ForwarderVehicleInfoForm form) {
        String url = urlBase + urlVehicleInfo;
        return post(form, url);
    }

    /**
     * ??????????????????
     *
     * @param form
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> forwarderLadingFile(ForwarderLadingFileForm form, MultipartFile file) {
        String url = urlBase + urlLadingFile;
        return postWithFile(form, file, url);
    }


    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public Map<String, Object> forwarderLadingFile(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);
        ForwarderLadingFileForm form = JSONUtil.toBean(jsonObject, ForwarderLadingFileForm.class);
        //????????????
//        CommonResult commonResult = form.checkParam();
//        if (commonResult != null) {
//            return commonResult;
//        }

        String filePath = jsonObject.getStr("filePath");
        String file = jsonObject.getStr("fileName");
        int count = file.lastIndexOf(".");
        String fileType = "";
        String fileName = file;
        if (count > 0) {
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1, file.length());
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        StringBuilder sb = new StringBuilder().append(form.getId())
                .append("_").append(fileName)
                .append("_");
        MultipartFile fileItem = FileUtil.createFileItem(filePath, sb.toString(), true, fileType);
        String url = urlBase + urlLadingFile;
        return postWithFile(form, fileItem, url);
    }

    @Override
    public Map<String, Object> forwarderLadingInfo(ForwarderLadingInfoForm form) {
        String url = urlBase + urlLadingInfo;
        return post(form, url);
    }

    /**
     * ??????????????????????????????vivo
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> forwarderAirFarePush(ForwarderAirFreightForm form) {
        String url = urlBase + urlAirFreightInfo;
        return post(form, url);
    }

    /**
     * ??????????????????????????????vivo
     *
     * @param form
     * @return
     */
    @Override
    public Map<String, Object> forwarderLandTransportationFarePush(ForwarderLandTransportationFareForm form) {
        String url = urlBase + urlLandTransportationCost;
        return post(form, url);
    }

    /**
     * ??????????????????
     */
    @Override
    public Map<String, Object> forwarderBookingRejected(String bookingNo, Integer status) {
        VivoBookingRejectedForm form = new VivoBookingRejectedForm();
        form.setBookingNo(bookingNo);
        form.setStatus(status);
        String url = urlBase + urlBookingRejected;
        Map<String, Object> resultMap = post(form, url);
        return resultMap;
    }

    /**
     * ????????????
     */
    @Override
    public Map<String, Object> forwarderDispatchRejected(DispatchRejectedForm form) {
        String url = urlBase + urlDispatchRejected;
        return post(form, url);
    }

    /**
     * ????????????
     */
    @Override
    public Map<String, Object> bookingRejected(AirOrder airOrder, AirCargoRejected airCargoRejected) {
        //???????????????????????????
        if (airCargoRejected.getRejectOptions() == 1) { //????????????
            Map<String, Object> map = new HashMap<>();
            map.put("orderNo", airOrder.getMainOrderNo());
            map.put("status", OrderStatusEnum.MAIN_8.getCode());
            ApiResult result = this.omsClient.updateByMainOrderNo(JSONUtil.toJsonStr(map));
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("??????????????????????????????????????? mainOrderNo={}", airOrder.getMainOrderNo());
                throw new JayudBizException(ResultEnum.OPR_FAIL);
            }
        }

        Integer status = airCargoRejected.getRejectOptions() == null ? VivoRejectionStatusEnum.PENDING_SUBMITTED.getCode()
                : airCargoRejected.getRejectOptions();
        //????????????,????????????
        if (VivoRejectionStatusEnum.PENDING_SUBMITTED.getCode().equals(status)) {
            this.airExtensionFieldService.updateByUniqueSign(airOrder.getThirdPartyOrderNo(),
                    new AirExtensionField().setStatus(StatusEnum.DELETE.getCode()));
        }

        Map<String, Object> resultMap = this.forwarderBookingRejected(airOrder.getThirdPartyOrderNo(), status);
        if (resultMap == null) {
            log.warn("??????vivo????????????????????????,??????????????????,???????????????");
            throw new JayudBizException(ResultEnum.VIVO_ERROR.getCode(), "??????vivo????????????????????????,??????????????????,???????????????");
        }
        if (1 != MapUtil.getInt(resultMap, "status")) {
            log.warn("??????vivo???????????????????????? message={}", MapUtil.getStr(resultMap, "message"));
            throw new JayudBizException(ResultEnum.VIVO_ERROR.getCode(), "??????vivo???????????????????????? message=" + MapUtil.getStr(resultMap, "message"));
        }

        return resultMap;
    }


    /**
     * ???????????????API??????
     *
     * @param form
     * @param url
     * @return
     */
    private Map<String, Object> post(Object form, String url) {
        Gson gson = new Gson();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> body = null;
//        body=new HttpEntity<MultiValueMap<String, String>>(JSONUtil.toBean(form,MultiValueMap.class),headers);

        String data = gson.toJson(form);
        Map<String, Object> map = this.doPost(data, url);
        if (map == null) {
            //??????????????????????????????
            map = doPost(data, url);
            if (map == null) {
                log.warn("?????????vivo??????");
                return new HashMap<>();
            }
        }
        //token??????,????????????
        if (map.get("Message") != null && map.get("Message").toString().contains("???????????????????????????")) {
            redisUtils.delete(VIVO_TOEKN_STR);
            map = this.doPost(data, url);
        }

//        String feedback = HttpRequest.post(url)
//                .header("Authorization", String.format(getToken(null, null, null)))
//                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
//                .form("transfer_data", gson.toJson(form))
//                .execute()
//                .body();

        //??????????????????????????????
//        if (StringUtils.isEmpty(feedback)) {
//            log.info("????????????vivo??????,?????? data={}", gson.toJson(form));
//            feedback = HttpRequest.post(url)
//                    .header("Authorization", String.format(getToken(null, null, null)))
//                    .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
//                    .form("transfer_data", gson.toJson(form))
//                    .execute()
//                    .body();
//            if (StringUtils.isEmpty(feedback)) {
//                log.warn("?????????vivo??????");
//                return new HashMap<>();
//            }
//        }

        return map;
    }


    private Map<String, Object> doPost(String form, String url) {
        log.info("vivo??????:" + form);
        String token = getToken(null, null, null);

        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", token)
                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                .form("transfer_data", form)
                .execute();
        String feedback = response.body();

        if (StringUtils.isEmpty(feedback)) {
            return null;
        }
//        log.info("??????:" + response.toString());
        log.info("??????token??????:" + token);
        log.info("vivo????????????:" + feedback);
        return JSONUtil.toBean(feedback, Map.class);
    }

    private Map<String, Object> postWithFile(Object form, MultipartFile file, String url) {
        Gson gson = new Gson();
        String data = gson.toJson(form);
        log.info("??????:" + data);
        File fw = new File(file.getOriginalFilename());
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), fw);
        } catch (IOException e) {
            log.warn("?????????????????????");
        }
        log.info("????????????====={}=======??????{}", FileUtils.sizeOf(fw), fw.getName());
        Map<String, Object> map = this.doPostWithFile(data, fw, url);
        if (map == null) {
            //??????????????????????????????
            map = doPostWithFile(data, fw, url);
            if (map == null) {
                log.warn("?????????vivo??????");
                return new HashMap<>();
            }
        }
        //token??????,????????????
        if (map.get("Message") != null && map.get("Message").toString().contains("???????????????????????????")) {
            redisUtils.delete(VIVO_TOEKN_STR);
            map = this.doPostWithFile(data, fw, url);
        }
        return map;
    }

    private Map<String, Object> doPostWithFile(String form, File fw, String url) {
        String feedback = HttpRequest.post(url)
                .header("Authorization", getToken(null, null, null))
                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                .form("transfer_data", form)
                .form("MultipartFile", fw)
                .execute()
                .body();
        if (StringUtils.isEmpty(feedback)) {
            return null;
        }
        log.info("????????????:" + feedback);
        if (fw.exists()) {
            fw.delete();
        }
        return JSONUtil.toBean(feedback, Map.class);
    }

    /**
     * ??????????????????????????????token
     *
     * @param userName
     * @param password
     * @param scope
     * @return
     */
    private String getToken(String userName, String password, String scope) {
        String vivoToekn = redisUtils.get(VIVO_TOEKN_STR);
        if (vivoToekn != null) {
            return vivoToekn;
        }

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(scope)) {
            //????????????????????????????????????????????????????????????
            userName = defaultUsername;
            password = defaultPassword;
            scope = defaultScope;
            log.info("????????????????????????????????????????????????vivo????????????...");
            String encryptedPassword = null;
            try {
                encryptedPassword = RsaEncryptUtil.getEncryptedPassword(password, publicKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (StringUtils.isBlank(encryptedPassword)) {
                Asserts.fail(ResultEnum.UNAUTHORIZED, "VIVO RSA????????????");
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
                redisUtils.set(VIVO_TOEKN_STR, String.format("bearer %s", access_token), 82800);
                return String.format("bearer %s", access_token);
            }

            Asserts.fail(ResultEnum.UNAUTHORIZED, "vivo ????????????");
        }
        return null;
    }


    private Boolean check4Success(String feedback) {
        Map<String, Object> map = JSONUtil.toBean(feedback, Map.class);

        Integer status = MapUtil.getInt(map, "status");
        String message = MapUtil.getStr(map, "message");

        if (Objects.isNull(status) || Objects.isNull(message)) {
            Asserts.fail(ResultEnum.PARAM_ERROR, "??????????????????");
        }
        if (status == 0) {
            return false;
        }
        return true;
    }

    /**
     * ????????????????????????????????????
     */
    @Override
    public JSONObject getCustomerInfoByLoginUserName() {
        //????????????id
        ApiResult result = this.oauthClient.getSystemUserByName(UserOperator.getToken());
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message={}", result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject systemUser = JSONUtil.parseObj(result.getData());
        Long companyId = systemUser.getLong("companyId");

        result = omsClient.getCustomerInfoById(companyId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message={}", result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject customerInfo = JSONUtil.parseObj(result.getData());
        return customerInfo;
    }


    /**
     * ??????????????????
     */
    @Override
    @Transactional
    public ApiResult createAirOrder(BookingSpaceForm form) {
        //??????booking,??????????????????id
        this.supplementIdOpt(form);

        InputOrderForm orderForm = new InputOrderForm();
        //??????????????????
        JSONObject customerInfo = this.getCustomerInfoByLoginUserName();
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //???????????????????????????
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.KY.getCode());
        mainOrderForm.setSelectedServer(OrderStatusEnum.KYDD.getCode());
        mainOrderForm.setCreateUserType(CreateUserTypeEnum.VIVO.getCode());
        mainOrderForm.setOrderId(form.getMainOrderId());
        //TODO ????????????????????????????????????????????????
        //??????????????????
        AddAirOrderForm addAirOrderForm = form.convertAddAirOrderForm();
        orderForm.setOrderForm(mainOrderForm);
        orderForm.setAirOrderForm(addAirOrderForm);

        //??????vivo??????
        AirExtensionField field = new AirExtensionField();
        field.setValue(JSONUtil.toJsonStr(form));
        field.setThirdPartyUniqueSign(form.getBookingNo());
        field.setBusinessTable(SqlConstant.AIR_ORDER);
        field.setCreateTime(LocalDateTime.now());
        field.setType(ExtensionFieldTypeEnum.ONE.getCode());
        field.setCreateUserType(CreateUserTypeEnum.VIVO.getCode());
        field.setRemarks(VivoInterfaceDescEnum.ONE.getDesc());
        airExtensionFieldService.save(field);
        //????????????
        ApiResult result = this.omsClient.holdOrder(orderForm);
        return result;
    }

    /**
     * ??????????????????
     */
    @Override
    public ApiResult createTmsOrder(CardInfoToForwarderForm form, InputOrderTransportForm orderTransportForm) {
        InputOrderForm orderForm = new InputOrderForm();
        //??????????????????
        JSONObject customerInfo = this.getCustomerInfoByLoginUserName();
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //???????????????????????????
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.ZGYS.getCode());
        mainOrderForm.setSelectedServer("JD");//??????????????????
        orderForm.setOrderTransportForm(orderTransportForm);
        orderForm.setOrderForm(mainOrderForm);

        //??????vivo??????
//        AirExtensionField field = new AirExtensionField();
//        field.setValue(JSONUtil.toJsonStr(form));
//        field.setThirdPartyUniqueSign(form.getDispatchNo());
//        field.setBusinessTable(SqlConstant.ORDER_TRANSPORT);
//        field.setCreateTime(LocalDateTime.now());
//        field.setType(ExtensionFieldTypeEnum.ONE.getCode());
//        field.setCreateUserType(CreateUserTypeEnum.VIVO.getCode());
//        field.setRemarks(VivoInterfaceDescEnum.SIX.getDesc());
//        //????????????????????????
//        ApiResult apiResult = this.tmsClient.saveOrUpdateTmsExtensionField(JSONUtil.toJsonStr(field));
//        if (!apiResult.isOk()) {
//            log.error("???????????????????????? msg={}", apiResult.getMsg());
//            throw new VivoApiException(ResultEnum.OPR_FAIL.getMessage());
//        }

        Map<String, Object> map = new HashMap<>();
        map.put("value", JSONUtil.toJsonStr(form));
        map.put("thirdPartyUniqueSign", form.getDispatchNo());
        map.put("businessTable", SqlConstant.ORDER_TRANSPORT);
        map.put("createTime", LocalDateTime.now());
        map.put("type", ExtensionFieldTypeEnum.ONE.getCode());
        map.put("remarks", VivoInterfaceDescEnum.SIX.getDesc());
        map.put("createUserType", CreateUserTypeEnum.VIVO.getCode());
        //??????????????????
        ApiResult apiResult = this.tmsClient.saveOrUpdateTmsExtensionField(JSONUtil.toJsonStr(map));
        if (!apiResult.isOk()) {
            log.error("???????????????????????? msg={}", apiResult.getMsg());
            throw new VivoApiException(ResultEnum.OPR_FAIL.getMessage());
        }
        //????????????
        ApiResult result = this.omsClient.holdOrder(orderForm);
        return result;
    }

    /**
     * ????????????????????????
     *
     * @param airOrder
     * @param bookingFileTransferDataForm
     * @return
     */
    @Override
    public boolean bookingFile(AirOrder airOrder, BookingFileTransferDataForm bookingFileTransferDataForm) {
        //??????????????????
        AirExtensionField airExtensionField = new AirExtensionField()
                .setBusinessTable(SqlConstant.AIR_ORDER)
                .setBusinessId(airOrder.getId())
                .setThirdPartyUniqueSign(bookingFileTransferDataForm.getBookingNo())
                .setCreateTime(LocalDateTime.now())
                .setType(ExtensionFieldTypeEnum.TWO.getCode())
                .setCreateUserType(CreateUserTypeEnum.VIVO.getCode())
                .setValue(JSONUtil.toJsonStr(bookingFileTransferDataForm))
                .setRemarks(VivoInterfaceDescEnum.FOUR.getDesc());
        return this.airExtensionFieldService.save(airExtensionField);
        //??????????????????
//        return airBookingService.updateByAirOrderId(airOrder.getId(), new AirBooking().setStatus("0"));
    }


    @Override
    public void bookingMessagePush(AirOrder airOrder, AirBooking airBooking) {
//        Map<String, String> request = new HashMap<>();
//        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_ONE.getTopic());
//        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_ONE.getKey());
//        Map<String, Object> msg = new HashMap<>();
//        msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
//        msg.put("forwarderBookingNo", airOrder.getOrderNo());
//        msg.put("deliveryWarehouse", airBooking.getDeliveryWarehouse());
//        msg.put("deliveryWarehouseAddress", airBooking.getDeliveryAddress());
//        request.put("msg", JSONUtil.toJsonStr(msg));
//        msgClient.consume(request);
        ForwarderBookingConfirmedFeedbackForm form = new ForwarderBookingConfirmedFeedbackForm();
        form.setBookingNo(airOrder.getThirdPartyOrderNo());
        form.setForwarderBookingno(airOrder.getOrderNo());
        form.setDeliveryWarehouse(airBooking.getDeliveryWarehouse());
        form.setDeliveryWarehouseAddress(airBooking.getDeliveryAddress());
        Map<String, Object> result = this.forwarderBookingConfirmedFeedback(form);

        if (0 == MapUtil.getInt(result, "status")) {
            log.error("???????????????????????????????????????vivo?????? msg={}", MapUtil.getStr(result, "message"));
            throw new JayudBizException(ResultEnum.VIVO_ERROR.getCode(), MapUtil.getStr(result, "message"));
        }
    }

    /**
     * ????????????
     *
     * @param airOrder
     */
    @Override
    public void trackingPush(AirOrder airOrder) {
        if (OrderStatusEnum.AIR_A_0.getCode().equals(airOrder.getStatus())
                || OrderStatusEnum.AIR_A_1.getCode().equals(airOrder.getStatus())
                || OrderStatusEnum.AIR_A_2.getCode().equals(airOrder.getStatus())) {
            return;
        }
        AirBooking airBooking = this.airBookingService.getEnableByAirOrderId(airOrder.getId());
        Map<String, String> request = new HashMap();
        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_TWO.getTopic());
        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_TWO.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
        msg.put("forwarderBookingNo", airOrder.getOrderNo());
        msg.put("pickUpDate", DateUtils.LocalDateTime2Str(airOrder.getGoodTime(), "yyyy/M/dd HH:mm:ss"));
//        msg.put("masterAirwayBill", airBooking.getMainNo());
        if (airBooking != null) {
            msg.put("billOfLading", airBooking.getMainNo() + (StringUtils.isEmpty(airBooking.getSubNo()) ?
                    "" : "/" + airBooking.getSubNo()));
            msg.put("flightNo", airBooking.getFlight());
            msg.put("chargedWeight", airBooking.getBillingWeight());
            msg.put("blWeight", airBooking.getBillLadingWeight());
            msg.put("etd", DateUtils.LocalDateTime2Str(airBooking.getEtd(), "yyyy/M/dd HH:mm:ss"));
            msg.put("atd", DateUtils.LocalDateTime2Str(airBooking.getAtd(), "yyyy/M/dd HH:mm:ss"));
            msg.put("eta", DateUtils.LocalDateTime2Str(airBooking.getEta(), "yyyy/M/dd HH:mm:ss"));
            msg.put("ata", DateUtils.LocalDateTime2Str(airBooking.getAta(), "yyyy/M/dd HH:mm:ss"));
        }
        msg.put("inboundDate", getInboundDate(airOrder));//????????????
        msg.put("modeOfTransport", 1);//??????????????????????????????????????????1????????????2????????????3????????????4???
        request.put("msg", JSONUtil.toJsonStr(msg));
        msgClient.consume(request);
    }


    private Object getInboundDate(AirOrder airOrder) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", airOrder.getId());
        map.put("type", BusinessTypeEnum.KY.getCode());
        map.put("status", OrderStatusEnum.AIR_A_3.getCode());
        ApiResult result = this.omsClient.getLogisticsTrackNode(JSONUtil.toJsonStr(map));
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("??????????????????????????????");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONArray json = new JSONArray(result.getData());
        return json.size() > 0
                ? DateUtils.format(
                json.getJSONObject(0).getDate("operatorTime"), "yyyy/M/dd HH:mm:ss")
                : null;
    }

    /**
     * ??????????????????
     *
     * @param airOrder
     * @param airBooking
     */
    public void billLadingInfoPush(AirOrder airOrder, AirBooking airBooking) {
//        Map<String, String> request = new HashMap();
//        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_THREE.getTopic());
//        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_THREE.getKey());
        String[] filePaths = airBooking.getFilePath().split(",");
        String[] fileNames = airBooking.getFileName().split(",");
        ApiResult result = this.fileClient.getBaseUrl();
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("????????????????????????");
            throw new JayudBizException("????????????????????????");
        }

        //????????????
        List<Map<String, Object>> successData = new ArrayList<>();
        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            String fileName = fileNames[i];
            Map<String, Object> msg = new HashMap<>();
            msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
            msg.put("forwarderBookingNo", airOrder.getOrderNo());
            msg.put("fileType", 1);
            msg.put("id", new RandomGUID().toStringTwo());
            msg.put("operationType", "add");
            msg.put("filePath", result.getData() + filePath);
            msg.put("fileName", fileName);
//        request.put("msg", JSONUtil.toJsonStr(msg));
//        msgClient.consume(request);

            Map<String, Object> resultMap = this.forwarderLadingFile(msg);
            if (0 == MapUtil.getInt(resultMap, "status")) {
                log.error("vivo???????????????????????????????????? bookingNo={} file={} msg={}", airOrder.getThirdPartyOrderNo(),
                        fileName, MapUtil.getStr(resultMap, "message"));
                if (!CollectionUtil.isEmpty(successData)) {
                    successData.forEach(e -> {
                        e.put("operationType", "delete");
                        this.forwarderLadingFile(e);
                    });
                }
                throw new JayudBizException(ResultEnum.VIVO_ERROR.getCode(),
                        MapUtil.getStr(resultMap, "message"));
            } else {
                successData.add(msg);
            }
        }


    }

    private void supplementIdOpt(BookingSpaceForm form) {
        //??????booking?????????????????????????????????,??????????????????????????????
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(form.getBookingNo());
        if (airOrder != null) {
            form.setAirOrderId(airOrder.getId());
            //????????????id
            ApiResult<List<GoodsVO>> resultOne = this.omsClient.getGoodsByBusIds(Collections.singletonList(airOrder.getId())
                    , BusinessTypeEnum.KY.getCode());
            if (resultOne.getCode() != HttpStatus.SC_OK) {
                log.warn("???????????????????????? mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), resultOne.getMsg());
                throw new VivoApiException("????????????????????????");
            }
            List<GoodsVO> goodsVOs = resultOne.getData();
            form.setGoodsId(goodsVOs.get(0).getId());

            //??????????????????
            ApiResult<List<OrderAddressVO>> resultTwo = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(airOrder.getId())
                    , BusinessTypeEnum.KY.getCode());
            if (resultTwo.getCode() != HttpStatus.SC_OK) {
                log.warn("?????????????????????????????? mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), resultTwo.getMsg());
                throw new VivoApiException("????????????????????????");
            }
            List<OrderAddressVO> addressVOS = resultTwo.getData();
            form.setAddressIds(addressVOS.stream().map(OrderAddressVO::getId).collect(Collectors.toList()));
            //???????????????id
            ApiResult result = this.omsClient.getIdByOrderNo(airOrder.getMainOrderNo());
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("?????????????????????????????????id?????? mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), result.getMsg());
                throw new VivoApiException("????????????????????????");
            }

            form.setMainOrderId(Long.valueOf(result.getData().toString()));
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void pushExceptionFeedbackInfo(AirOrder airOrder, AddAirExceptionFeedbackForm form, AirExceptionFeedback airExceptionFeedback) {
        String[] filePaths = airExceptionFeedback.getFilePath().split(",");
        String[] fileNames = airExceptionFeedback.getFileName().split(",");
        ApiResult result = this.fileClient.getBaseUrl();
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("????????????????????????");
            throw new JayudBizException("????????????????????????");
        }

        for (int i = 0; i < filePaths.length; i++) {
            String filePath = filePaths[i];
            String fileName = fileNames[i];
            Map<String, Object> msg = new HashMap<>();
            msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
            msg.put("forwarderBookingNo", airOrder.getOrderNo());
            msg.put("fileType", 3);
            msg.put("id", new RandomGUID().toStringTwo());
            msg.put("operationType", "add");
            msg.put("filePath", result.getData() + filePath);
            msg.put("fileName", fileName);
            msg.put("abnormalyClassification", airExceptionFeedback.getType());
            msg.put("abnormal", airExceptionFeedback.getRemarks());
            msg.put("occurrenceTime", DateUtils.str2LocalDateTime(form.getStartTime(), "-", "/"));
            msg.put("exceptionFinishTime", DateUtils.str2LocalDateTime(form.getCompletionTime(), "-", "/"));
//        request.put("msg", JSONUtil.toJsonStr(msg));
//        msgClient.consume(request);
            //????????????
            List<Map<String, Object>> successData = new ArrayList<>();
            Map<String, Object> resultMap = this.forwarderLadingFile(msg);
            if (0 == MapUtil.getInt(resultMap, "status")) {
                log.error("[vivo]???????????????????????? bookingNo={} file={} msg={}", airOrder.getThirdPartyOrderNo(),
                        fileName, MapUtil.getStr(resultMap, "message"));

                if (!CollectionUtil.isEmpty(successData)) {
                    successData.forEach(e -> {
                        e.put("operationType", "delete");
                        this.forwarderLadingFile(e);
                    });
                }
                throw new JayudBizException(ResultEnum.VIVO_ERROR.getCode(),
                        MapUtil.getStr(resultMap, "message"));
            } else {
                successData.add(msg);
            }

        }
    }

    /**
     * ???????????????
     */
    @Override
    @Transactional
    public void bookingCancel(AirOrder airOrder) {
        //??????????????????
        //??????????????????????????????
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", airOrder.getMainOrderNo());
        map.put("status", OrderStatusEnum.MAIN_6.getCode());
        this.omsClient.updateByMainOrderNo(JSONUtil.toJsonStr(map));
        //????????????
        this.airOrderService.updateById(new AirOrder()
                .setId(airOrder.getId()).setProcessStatus(ProcessStatusEnum.CLOSE.getCode()));
    }


}
