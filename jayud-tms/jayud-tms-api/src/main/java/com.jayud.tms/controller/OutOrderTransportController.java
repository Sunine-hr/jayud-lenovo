package com.jayud.tms.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.aop.annotations.RepeatSubmitLimit;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.RSAUtils;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.feign.OmsMiniClient;
import com.jayud.tms.model.bo.*;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.OutOrderTransportVO;
import com.jayud.tms.service.IOrderSendCarsService;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ??????????????????????????????????????????????????????????????????
 */
@RestController
@Api(tags = "????????????????????????")
@RequestMapping("/orderTransport")
@Slf4j
public class OutOrderTransportController {

    @Autowired
    IOrderTransportService orderTransportService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    OmsClient omsClient;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    OmsMiniClient omsMiniClient;
    @Autowired
    IOrderSendCarsService orderSendCarsService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     ???????????????
     */
    @Value("${scm.default.loginName:}")
    private String defaultLoginNme;

    @ApiOperation(value = "?????????????????????????????????")
    @RequestMapping(value = "/createOutOrderTransport")
    String createOutOrderTransport(@RequestBody Map<String, Object> param) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String appId = MapUtil.getStr(param, "appId");
        //??????????????????
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");
        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //??????????????????????????????
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));

        com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutTransportForm.class);


        InputOrderOutTransportForm form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutTransportForm.class);


        if (log.isInfoEnabled()) {
            log.info("??????????????????????????????????????????{}", JSONUtil.toJsonStr(form));
        }
        //??????????????????
        if (form == null) {
            return  privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }
        //????????????
        form.checkOrderTransportParam();

        String key = "SCM-" + form.getOrderNo();
        int limit = redisTemplate.opsForValue().increment(key, 1).intValue();
        redisTemplate.expire(key, 20, TimeUnit.SECONDS);
        if (limit > 1) {
            log.warn("?????????????????? message=???????????????????????????");
            return privatekey(ApiResult.error("???????????????????????????"), appPrivateSecret);
        }

        ApiResult apiResult = null;
        try {
            OutOrderTransportVO outOrderTransportVO = orderTransportService.getOutOrderTransportVOByThirdPartyOrderNo(form.getOrderNo());
            if (outOrderTransportVO != null) {
                log.warn("?????????????????? message=???????????????????????????");
                return privatekey(ApiResult.error("???????????????????????????"), appPrivateSecret);
            }
            apiResult = this.createOrUpdateOutOrderTransport(form, outOrderTransportVO, false,companyId);
        } catch (Exception e) {
            redisUtils.delete(key);
            throw e;
        } finally {
            if (apiResult != null && apiResult.getCode() != HttpStatus.SC_OK) {
                redisUtils.delete(key);
            }
        }
        return privatekey(apiResult, appPrivateSecret);
    }

    @ApiOperation(value = "?????????????????????????????????")
    @RequestMapping(value = "/updateOutOrderTransport")
    String updateOutOrderTransport(@RequestBody Map<String, Object> param) throws  Exception, NoSuchAlgorithmException {

        String appId = MapUtil.getStr(param, "appId");
        //??????????????????
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");
        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //??????????????????????????????
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));

//        com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutTransportForm.class);


        InputOrderOutTransportForm form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutTransportForm.class);

        if (log.isInfoEnabled()) {
            log.info("??????????????????????????????????????????{}", JSONUtil.toJsonStr(form));
        }

        //??????????????????
        if (form == null) {
            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }

        //????????????
        form.checkOrderTransportParam();

        OutOrderTransportVO outOrderTransportVO = orderTransportService.getOutOrderTransportVOByThirdPartyOrderNo(form.getOrderNo());
        if (outOrderTransportVO == null) {
            log.warn("?????????????????? message=??????????????????????????????????????????????????????");

            return privatekey(ApiResult.error("????????????????????????"), appPrivateSecret);
        }
        ApiResult orUpdateOutOrderTransport = this.createOrUpdateOutOrderTransport(form, outOrderTransportVO, true, companyId);
        return privatekey(orUpdateOutOrderTransport, appPrivateSecret);

    }


    @ApiOperation(value = "?????????????????????????????????")
    @RequestMapping(value = "/deleteOutOrderTransport")
    String deleteOutOrderTransport(@RequestBody Map<String, Object> param) throws InvalidKeySpecException, NoSuchAlgorithmException {

        String appId = MapUtil.getStr(param, "appId");
        //??????????????????
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");

        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("???????????????????????????????????? message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //??????????????????????????????
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));



        InputOrderOutTransportForm form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutTransportForm.class);

        if (log.isInfoEnabled()) {
            log.info("??????????????????????????????????????????{}", JSONUtil.toJsonStr(form));
        }

        //??????????????????
        if (form == null) {
            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }

        OutOrderTransportVO outOrderTransportVO = orderTransportService.getOutOrderTransportVOByThirdPartyOrderNo(form.getOrderNo());
        if (outOrderTransportVO == null) {
            log.warn("?????????????????? message=??????????????????????????????????????????????????????");

            return privatekey(ApiResult.error("????????????????????????"), appPrivateSecret);
        }
        //???????????????????????????????????????
        ApiResult apiResult = omsClient.deleteOrderInfoUpdateByIdOne(outOrderTransportVO.getMainOrderNo());

        OrderTransport orderTransport = new OrderTransport();
        orderTransport.setId(outOrderTransportVO.getId());
        orderTransport.setThirdPartyOrderNo("");

        this.orderTransportService.updateById(orderTransport);


        return privatekey(apiResult, appPrivateSecret);

    }




    /**
     * ???????????????????????????
     * @param form
     * @param isEdit
     * @return
     */
    private ApiResult createOrUpdateOutOrderTransport(InputOrderOutTransportForm form, OutOrderTransportVO outOrderTransportVO, boolean isEdit,Long companyId){
        String tip = isEdit ? "??????": "??????";
        InputOrderForm orderForm = new InputOrderForm();
        //??????????????????
        JSONObject customerInfo = orderTransportService.getCustomerInfoByLoginUserName(companyId);
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //???????????????????????????
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.ZGYS.getCode());
        mainOrderForm.setSelectedServer(OrderStatusEnum.ZGYS.getCode());
        mainOrderForm.setCreateUserType(CreateUserTypeEnum.SCM.getCode());
        mainOrderForm.setBizUid(customerInfo.getLong("ywId"));
        mainOrderForm.setBizUname(customerInfo.getStr("ywName"));

        if (isEdit) {
            mainOrderForm.setOrderId(outOrderTransportVO.getMainOrderId());
            mainOrderForm.setOrderNo(outOrderTransportVO.getMainOrderNo());
        }

        // ???????????????????????????????????????????????????
        ApiResult<String> productBizIdCodeResult = omsClient.getProductBizIdCodeByName("????????????");
        if (productBizIdCodeResult.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????????????????????????????? message={}", productBizIdCodeResult.getMsg());
            return ApiResult.error(tip + "????????????");
        }
        if (productBizIdCodeResult.getData() == null) {
            log.warn("?????????????????????????????? message={}", productBizIdCodeResult.getMsg());
            return ApiResult.error("??????????????????????????????");
        }
        mainOrderForm.setBizCode(productBizIdCodeResult.getData());
        mainOrderForm.setBizBelongDepart(customerInfo.getLong("departmentId"));
        mainOrderForm.setUnitAccount(customerInfo.getStr("name"));
        mainOrderForm.setUnitCode(customerInfo.getStr("idCode"));
        mainOrderForm.setOperationTime(LocalDateTime.now());
        mainOrderForm.setCmd("preSubmit");
        mainOrderForm.setReferenceNo(form.getOrderNo());

        //??????????????????
        InputOrderTransportForm inputOrderTransportForm = new InputOrderTransportForm();
        //??????????????????
        ApiResult portResult = this.omsClient.getPortCodeByName(form.getPortName());
        if (portResult.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????????????????????????????? message={}", portResult.getMsg());
            return ApiResult.error(tip + "????????????");
        }
        if (portResult.getData() == null) {
            log.warn("?????????????????????????????? message={}", portResult.getMsg());
            return ApiResult.error("??????????????????????????????");
        }

        inputOrderTransportForm.setPortCode(String.valueOf(portResult.getData()));
        inputOrderTransportForm.setGoodsType(form.getGoodsTypeVal());
        inputOrderTransportForm.setUnitCode(mainOrderForm.getUnitCode());
        inputOrderTransportForm.setThirdPartyOrderNo(form.getOrderNo());
        inputOrderTransportForm.setIsVehicleWeigh(form.getIsVehicleWeigh());
        inputOrderTransportForm.setVehicleType(form.getVehicleTypeVal());
        inputOrderTransportForm.setVehicleSize(form.getPreTruckStyle());
        inputOrderTransportForm.setCreateUserType(mainOrderForm.getCreateUserType());
        inputOrderTransportForm.setIsLoadGoods("0");
        inputOrderTransportForm.setIsUnloadGoods("0");
        // ????????????
        ApiResult<Long> warehouseIdResult = omsClient.getWarehouseIdByName(form.getWarehouseInfo());
        if (warehouseIdResult.getCode() != HttpStatus.SC_OK) {
            log.warn("????????????????????????????????????ID?????????????????? message={}", warehouseIdResult.getMsg());
            return ApiResult.error(tip + "????????????");
        }
        if (warehouseIdResult.getData() == null) {
            log.warn("???????????????????????? message={}", warehouseIdResult.getMsg());
            return ApiResult.error("??????????????????????????????");
        }
        inputOrderTransportForm.setWarehouseInfoId(warehouseIdResult.getData());

        LocalDateTime truckDate = LocalDateTime.parse(form.getTruckDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        LocalDateTime receivingDate = truckDate.withHour(18).withMinute(0).withSecond(0);

        // ????????????
        if (CollectionUtil.isNotEmpty(form.getTakeAdrForms1())) {
            List<InputOrderTakeAdrForm> orderTakeAdrForms1 = new ArrayList<>();
            for (InputOrderOutTakeAdrForm orderTakeAdrForm1 : form.getTakeAdrForms1()) {
                InputOrderTakeAdrForm inputOrderTakeAdrForm = ConvertUtil.convert(orderTakeAdrForm1, InputOrderTakeAdrForm.class);

                if (!StringUtils.isAnyBlank(orderTakeAdrForm1.getProvinceName(),
                        orderTakeAdrForm1.getAreaName(),
                        orderTakeAdrForm1.getCityName())) {
                    ApiResult<Map<String, Long>> regionCityIdMap = omsClient.getRegionCityIdMapByName(orderTakeAdrForm1.getProvinceName(),
                            orderTakeAdrForm1.getCityName(), orderTakeAdrForm1.getAreaName());
                    if (regionCityIdMap.getCode() != HttpStatus.SC_OK) {
                        log.warn("?????????????????????????????????????????????Map?????????????????? message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("???????????????????????? message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("????????????????????????");
                    }

                    inputOrderTakeAdrForm.setProvince(regionCityIdMap.getData().get(orderTakeAdrForm1.getProvinceName()));
                    inputOrderTakeAdrForm.setCity(regionCityIdMap.getData().get(orderTakeAdrForm1.getCityName()));
                    inputOrderTakeAdrForm.setArea(regionCityIdMap.getData().get(orderTakeAdrForm1.getAreaName()));
                }

                // ????????????
                inputOrderTakeAdrForm.setTakeTimeStr(truckDate);

                inputOrderTakeAdrForm.setGoodsDesc(orderTakeAdrForm1.getGoodsDesc());//????????????
                inputOrderTakeAdrForm.setPlateAmount(orderTakeAdrForm1.getPieceAmount());//??????
                inputOrderTakeAdrForm.setPieceAmount(orderTakeAdrForm1.getBulkCargoAmount());//??????
                inputOrderTakeAdrForm.setWeight(orderTakeAdrForm1.getWeight());//??????
                inputOrderTakeAdrForm.setVolume(orderTakeAdrForm1.getVolume());//??????
                orderTakeAdrForms1.add(inputOrderTakeAdrForm);
            }
            inputOrderTransportForm.setTakeAdrForms1(orderTakeAdrForms1);
        }

        // ????????????
        if (CollectionUtil.isNotEmpty(form.getTakeAdrForms2())) {
            List<InputOrderTakeAdrForm> orderTakeAdrForms2 = new ArrayList<>();
            for (InputOrderOutTakeAdrForm orderTakeAdrForm2 : form.getTakeAdrForms2()) {
                InputOrderTakeAdrForm inputOrderTakeAdrForm = ConvertUtil.convert(orderTakeAdrForm2, InputOrderTakeAdrForm.class);

                if (!StringUtils.isAnyBlank(orderTakeAdrForm2.getProvinceName(),
                        orderTakeAdrForm2.getAreaName(),
                        orderTakeAdrForm2.getCityName())) {
                    ApiResult<Map<String, Long>> regionCityIdMap = omsClient.getRegionCityIdMapByName(orderTakeAdrForm2.getProvinceName(),
                            orderTakeAdrForm2.getCityName(), orderTakeAdrForm2.getAreaName());
                    if (regionCityIdMap.getCode() != HttpStatus.SC_OK) {
                        log.warn("?????????????????????????????????????????????Map?????????????????? message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("???????????????????????? message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("????????????????????????");
                    }
                    inputOrderTakeAdrForm.setProvince(regionCityIdMap.getData().get(orderTakeAdrForm2.getProvinceName()));
                    inputOrderTakeAdrForm.setCity(regionCityIdMap.getData().get(orderTakeAdrForm2.getCityName()));
                    inputOrderTakeAdrForm.setArea(regionCityIdMap.getData().get(orderTakeAdrForm2.getAreaName()));
                }

                // ????????????
                inputOrderTakeAdrForm.setTakeTimeStr(receivingDate);
                inputOrderTakeAdrForm.setGoodsDesc(orderTakeAdrForm2.getGoodsDesc());//????????????
                inputOrderTakeAdrForm.setPlateAmount(orderTakeAdrForm2.getPieceAmount());//??????
                inputOrderTakeAdrForm.setPieceAmount(orderTakeAdrForm2.getBulkCargoAmount());//??????
                inputOrderTakeAdrForm.setWeight(orderTakeAdrForm2.getWeight());//??????
                inputOrderTakeAdrForm.setVolume(orderTakeAdrForm2.getVolume());//??????
                orderTakeAdrForms2.add(inputOrderTakeAdrForm);
            }
            inputOrderTransportForm.setTakeAdrForms2(orderTakeAdrForms2);
        }

        // ????????????
        String legalEntityIdStr = customerInfo.getStr("legalEntityIdStr");
        inputOrderTransportForm.setLegalEntityId(Long.parseLong(legalEntityIdStr.split(",")[0]));
        inputOrderTransportForm.setLegalName(oauthClient.getLegalNameByLegalId(inputOrderTransportForm.getLegalEntityId()).getData());
        inputOrderTransportForm.setDepartmentId(customerInfo.getLong("departmentId").intValue());
        // ????????????
        mainOrderForm.setLegalName(inputOrderTransportForm.getLegalName());
        mainOrderForm.setLegalEntityId(inputOrderTransportForm.getLegalEntityId());
        // 1?????????????????????????????????
        mainOrderForm.setIsDataAll("");

        if (isEdit) {
            // ??????????????????
            inputOrderTransportForm.setId(outOrderTransportVO.getId());
            inputOrderTransportForm.setOrderNo(outOrderTransportVO.getOrderNo());
        }

        orderForm.setCmd(mainOrderForm.getCmd());
        orderForm.setOrderForm(mainOrderForm);
        orderForm.setOrderTransportForm(inputOrderTransportForm);
        //?????????????????????
        orderForm.setLoginUserName(defaultLoginNme);
        // ???????????????????????????
        return omsClient.saveOrUpdateOutMainOrderForm(orderForm);
    }

    //??????????????????
    public String privatekey(ApiResult booleanApiResult, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String s1 = com.alibaba.fastjson.JSONObject.toJSONString(booleanApiResult);
        return RSAUtils.privateEncrypt(s1, RSAUtils.getPrivateKey(privateKey));
    }
}

