package com.jayud.airfreight.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.jayud.airfreight.feign.*;
import com.jayud.airfreight.model.bo.AddAirOrderForm;
import com.jayud.airfreight.model.bo.InputMainOrderForm;
import com.jayud.airfreight.model.bo.InputOrderForm;
import com.jayud.airfreight.model.bo.InputOrderTransportForm;
import com.jayud.airfreight.model.bo.vivo.*;
import com.jayud.airfreight.model.po.AirBooking;
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
     * 向vivo发送确认订舱的数据
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
     * 货代车辆信息
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
     * 提单文件信息
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

    @Override
    public Map<String, Object> forwarderLadingInfo(ForwarderLadingInfoForm form) {
        String url = urlBase + urlLadingInfo;
        return post(form, url);
    }

    /**
     * 货代抛空运费用数据到vivo
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
     * 货代抛陆运费用数据到vivo
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
     * 订舱驳回接口
     */
    public Map<String, Object> forwarderBookingRejected(String bookingNo, Integer status) {
        VivoBookingRejectedForm form = new VivoBookingRejectedForm();
        form.setBookingNo(bookingNo);
        form.setStatus(status);
        String url = urlBase + urlBookingRejected;
        Map<String, Object> resultMap = post(form, url);
        return resultMap;
    }

    /**
     * 派车驳回
     */
    @Override
    public Map<String, Object> forwarderDispatchRejected(DispatchRejectedForm form) {
        String url = urlBase + urlDispatchRejected;
        return post(form, url);
    }


    /**
     * 向联想发送API请求
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
        log.info("vivo参数==========" + data);

        Map<String, Object> map = this.doPost(data, url);
        if (map == null) {
            //没有返回重新调用一次
            map = doPost(data, url);
            if (map == null) {
                log.warn("请联系vivo客户");
                return new HashMap<>();
            }
        }
        //token过期,重新请求
        if ((map.get("Message").toString().contains("已拒绝为此请求授权"))) {
            redisUtils.delete(VIVO_TOEKN_STR);
            map = this.doPost(data, url);
        }

//        String feedback = HttpRequest.post(url)
//                .header("Authorization", String.format(getToken(null, null, null)))
//                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
//                .form("transfer_data", gson.toJson(form))
//                .execute()
//                .body();

        //没有返回重新调用一次
//        if (StringUtils.isEmpty(feedback)) {
//            log.info("重试调用vivo接口,参数 data={}", gson.toJson(form));
//            feedback = HttpRequest.post(url)
//                    .header("Authorization", String.format(getToken(null, null, null)))
//                    .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
//                    .form("transfer_data", gson.toJson(form))
//                    .execute()
//                    .body();
//            if (StringUtils.isEmpty(feedback)) {
//                log.warn("请联系vivo客户");
//                return new HashMap<>();
//            }
//        }

        return map;
    }


    private Map<String, Object> doPost(String form, String url) {
        log.info("vivo参数==========" + form);
        String feedback = HttpRequest.post(url)
                .header("Authorization", String.format(getToken(null, null, null)))
                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                .form("transfer_data", form)
                .execute()
                .body();
        if (StringUtils.isEmpty(feedback)) {
            return null;
        }
        return JSONUtil.toBean(feedback, Map.class);
    }

    private Map<String, Object> postWithFile(Object form, MultipartFile file, String url) {
        Gson gson = new Gson();
        String data = gson.toJson(form);
        log.info("参数========" + data);
        File fw = new File(file.getOriginalFilename());
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), fw);
        } catch (IOException e) {
            log.warn("文件流操作失败");
        }
        log.info("文件大小====={}=======名称{}", FileUtils.sizeOf(fw), fw.getName());
        Map<String, Object> map = this.doPostWithFile(data, fw, url);
        if (map == null) {
            //没有返回重新调用一次
            map = doPostWithFile(data, fw, url);
            if (map == null) {
                log.warn("请联系vivo客户");
                return new HashMap<>();
            }
        }
        //token过期,重新请求
        if ((map.get("Message").toString().contains("已拒绝为此请求授权"))) {
            redisUtils.delete(VIVO_TOEKN_STR);
            map = this.doPostWithFile(data, fw, url);
        }

        return map;
    }

    private Map<String, Object> doPostWithFile(String form, File fw, String url) {
        String feedback = HttpRequest.post(url)
                .header("Authorization", String.format(getToken(null, null, null)))
                .header(Header.CONTENT_TYPE.name(), "multipart/form-data")
                .form("transfer_data", form)
                .form("MultipartFile", fw)
                .execute()
                .body();
        if (StringUtils.isEmpty(feedback)) {
            return null;
        }
        return JSONUtil.toBean(feedback, Map.class);
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
        String vivoToekn = redisUtils.get(VIVO_TOEKN_STR);
        if (vivoToekn != null) {
            return vivoToekn;
        }

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
                redisUtils.set(VIVO_TOEKN_STR, access_token, 82800);
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


    /**
     * 创建空运订单
     */
    @Override
    @Transactional
    public ApiResult createAirOrder(BookingSpaceForm form) {
        //存在booking,设置更新操作id
        this.supplementIdOpt(form);

        InputOrderForm orderForm = new InputOrderForm();
        //查询客户名称
        JSONObject customerInfo = this.getCustomerInfoByLoginUserName();
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //主订单设置客户名称
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.KY.getCode());
        mainOrderForm.setSelectedServer(OrderStatusEnum.KYDD.getCode());
        //TODO 不清楚接单法人和结算单位是否要传
        //组装空运订单
        AddAirOrderForm addAirOrderForm = form.convertAddAirOrderForm();
        orderForm.setOrderForm(mainOrderForm);
        orderForm.setAirOrderForm(addAirOrderForm);

        //保存vivo字段
        AirExtensionField field = new AirExtensionField();
        field.setValue(JSONUtil.toJsonStr(form));
        field.setThirdPartyUniqueSign(form.getBookingNo());
        field.setBusinessTable(SqlConstant.AIR_ORDER);
        field.setCreateTime(LocalDateTime.now());
        field.setType(ExtensionFieldTypeEnum.VIVO.getCode());
        field.setRemarks(VivoInterfaceDescEnum.ONE.getDesc());
        airExtensionFieldService.save(field);
        //暂存订单
        ApiResult result = this.omsClient.holdOrder(orderForm);
        return result;
    }

    /**
     * 创建中港订单
     */
    @Override
    public ApiResult createTmsOrder(CardInfoToForwarderForm form, InputOrderTransportForm orderTransportForm) {
        InputOrderForm orderForm = new InputOrderForm();
        //查询客户名称
        JSONObject customerInfo = this.getCustomerInfoByLoginUserName();
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //主订单设置客户名称
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.ZGYS.getCode());
        mainOrderForm.setSelectedServer("JD");//选择服务类型
        orderForm.setOrderTransportForm(orderTransportForm);
        orderForm.setOrderForm(mainOrderForm);


        //保存vivo字段
        AirExtensionField field = new AirExtensionField();
        field.setValue(JSONUtil.toJsonStr(form));
        field.setThirdPartyUniqueSign(form.getDispatchNo());
        field.setBusinessTable(SqlConstant.ORDER_TRANSPORT);
        field.setCreateTime(LocalDateTime.now());
        field.setType(ExtensionFieldTypeEnum.VIVO.getCode());
        field.setRemarks(VivoInterfaceDescEnum.SIX.getDesc());
        airExtensionFieldService.save(field);

        Map<String, Object> map = new HashMap<>();
        map.put("value", JSONUtil.toJsonStr(form));
        map.put("thirdPartyUniqueSign", form.getDispatchNo());
        map.put("businessTable", SqlConstant.ORDER_TRANSPORT);
        map.put("createTime", LocalDateTime.now());
        map.put("type", ExtensionFieldTypeEnum.VIVO.getCode());
        map.put("remarks", VivoInterfaceDescEnum.SIX.getDesc());
        //保存冗余字段
        this.tmsClient.saveOrUpdateTmsExtensionField(JSONUtil.toJsonStr(map));
        //暂存订单
        ApiResult result = this.omsClient.holdOrder(orderForm);
        return result;
    }

    /**
     * 货代获取订舱文件
     *
     * @param airOrder
     * @param bookingFileTransferDataForm
     * @return
     */
    @Override
    @Transactional
    public boolean bookingFile(AirOrder airOrder, BookingFileTransferDataForm bookingFileTransferDataForm) {
        //存储冗余字段
        AirExtensionField airExtensionField = new AirExtensionField()
                .setBusinessTable(SqlConstant.AIR_BOOKING)
                .setBusinessId(airOrder.getId())
                .setThirdPartyUniqueSign(bookingFileTransferDataForm.getBookingNo())
                .setCreateTime(LocalDateTime.now())
                .setType(BusinessTypeEnum.KY.getCode())
                .setValue(JSONUtil.toJsonStr(bookingFileTransferDataForm))
                .setRemarks(VivoInterfaceDescEnum.FOUR.getDesc());
        this.airExtensionFieldService.save(airExtensionField);
        //修改订舱状态
        return airBookingService.updateByAirOrderId(airOrder.getId(), new AirBooking().setStatus("0"));
    }

    @Override
    public void bookingMessagePush(AirOrder airOrder, AirBooking airBooking) {
        Map<String, String> request = new HashMap();
        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_ONE.getTopic());
        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_ONE.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
        msg.put("forwarderBookingNo", airOrder.getOrderNo());
        msg.put("deliveryWarehouse", airBooking.getDeliveryWarehouse());
        msg.put("deliveryWarehouseAddress", airBooking.getDeliveryAddress());
        request.put("msg", JSONUtil.toJsonStr(msg));
        msgClient.consume(request);
//            if (ResultEnum.INTERNAL_SERVER_ERROR.getCode().toString().equals(consume.get("code"))) {
//                log.error("远程调用推送确认订舱信息给vivo失败 data={}", JSONUtil.toJsonStr(msg));
//                throw new JayudBizException(ResultEnum.OPR_FAIL);
//            }
    }

    /**
     * 跟踪推送
     *
     * @param airOrder
     */
    @Override
    public void trackingPush(AirOrder airOrder) {
        AirBooking airBooking = this.airBookingService.getByAirOrderId(airOrder.getId());
        Map<String, String> request = new HashMap();
        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_TWO.getTopic());
        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_TWO.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
        msg.put("forwarderBookingNo", airOrder.getOrderNo());
        msg.put("pickUpDate", DateUtils.LocalDateTime2Str(airOrder.getGoodTime(), "yyyy/M/dd HH:mm:ss"));
//        msg.put("masterAirwayBill", airBooking.getMainNo());
        msg.put("billOfLading", airBooking.getMainNo() + "/" + (StringUtils.isEmpty(airBooking.getSubNo()) ?
                "" : airBooking.getSubNo()));
        msg.put("flightNo", airBooking.getFlight());
//            msg.put("chargedWeight", "");
//            msg.put("bLWeight", "");
        msg.put("etd", DateUtils.LocalDateTime2Str(airBooking.getEtd(), "yyyy/M/dd HH:mm:ss"));
        msg.put("atd", DateUtils.LocalDateTime2Str(airBooking.getAtd(), "yyyy/M/dd HH:mm:ss"));
        msg.put("eta", DateUtils.LocalDateTime2Str(airBooking.getEta(), "yyyy/M/dd HH:mm:ss"));
        msg.put("ata", DateUtils.LocalDateTime2Str(airBooking.getAta(), "yyyy/M/dd HH:mm:ss"));
        msg.put("inboundDate", getInboundDate(airOrder));//入仓时间
        msg.put("modeOfTransport", 1);//空运跟踪表中运输方式（空运：1；铁运：2；海运：3；陆运：4）
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
            log.error("获取物流轨迹节点失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONArray json = new JSONArray(result.getData());
        return json.size() > 0
                ? DateUtils.format(
                json.getJSONObject(0).getDate("operatorTime"), "yyyy/M/dd HH:mm:ss")
                : null;
    }

    /**
     * 推送提单信息
     *
     * @param airOrder
     * @param airBooking
     */
    public void billLadingInfoPush(AirOrder airOrder, AirBooking airBooking) {
        Map<String, String> request = new HashMap();
        request.put("topic", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_THREE.getTopic());
        request.put("key", KafkaMsgEnums.VIVO_FREIGHT_AIR_MESSAGE_THREE.getKey());
        Map<String, Object> msg = new HashMap<>();
        msg.put("bookingNo", airOrder.getThirdPartyOrderNo());
        msg.put("forwarderBookingNo", airOrder.getOrderNo());
        msg.put("fileType", 1);
        msg.put("id", new RandomGUID().toStringTwo());
        msg.put("operationType", "add");
        ApiResult result = this.fileClient.getBaseUrl();
        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("获取文件地址失败");
            throw new JayudBizException("获取文件地址失败");
        }
        msg.put("filePath", result.getData() + airBooking.getFilePath());
        msg.put("fileName", airBooking.getFileName());
        request.put("msg", JSONUtil.toJsonStr(msg));
        msgClient.consume(request);
    }

    private void supplementIdOpt(BookingSpaceForm form) {
        //根据booking号查询是否存在空运订单,如果存在进行订单更新
        AirOrder airOrder = this.airOrderService.getByThirdPartyOrderNo(form.getBookingNo());
        if (airOrder != null) {
            form.setAirOrderId(airOrder.getId());
            //查询商品id
            ApiResult<List<GoodsVO>> resultOne = this.omsClient.getGoodsByBusIds(Collections.singletonList(airOrder.getId())
                    , BusinessTypeEnum.KY.getCode());
            if (resultOne.getCode() != HttpStatus.SC_OK) {
                log.warn("查询商品信息失败 mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), resultOne.getMsg());
                throw new VivoApiException("查询商品信息失败");
            }
            List<GoodsVO> goodsVOs = resultOne.getData();
            form.setGoodsId(goodsVOs.get(0).getId());

            //查询地址信息
            ApiResult<List<OrderAddressVO>> resultTwo = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(airOrder.getId())
                    , BusinessTypeEnum.KY.getCode());
            if (resultTwo.getCode() != HttpStatus.SC_OK) {
                log.warn("查询订单地址信息失败 mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), resultTwo.getMsg());
                throw new VivoApiException("查询订单地址信息失败");
            }
            List<OrderAddressVO> addressVOS = resultTwo.getData();
            form.setAddressIds(addressVOS.stream().map(OrderAddressVO::getId).collect(Collectors.toList()));
            //查询主订单id
            ApiResult result = this.omsClient.getIdByOrderNo(airOrder.getMainOrderNo());
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("根据主订单号查询主订单id失败 mainOrderNo={} msg={}",
                        airOrder.getMainOrderNo(), result.getMsg());
                throw new VivoApiException("空运订单更新失败");
            }
            form.setMainOrderId(Long.valueOf(result.getData().toString()));
        }
    }

    public static void main(String[] args) {
//        UUID uuid = UUID.randomUUID();
//        System.out.println(uuid.toString());

        Map<String, Object> map = new HashMap<>();
        map.put("Message", "已拒绝为此请求授权。");
        System.out.println(map.get("Message").toString().contains("已拒绝为此请求授权"));
    }
}
