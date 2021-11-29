package com.jayud.Inlandtransport.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.model.bo.*;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.jayud.Inlandtransport.model.vo.OutOrderInlandTransportVO;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.entity.AuditInfoForm;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.RSAUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 外部内陆订单接口，该接口由供应商系统发起调用
 */
@RestController
@Api(tags = "外部内陆订单接口")
@RequestMapping("/orderInlandApi")
@Slf4j
public class OutOrderInlandController {

    @Autowired
    IOrderInlandTransportService orderInlandTransportService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    OmsClient omsClient;
    @Autowired
    OauthClient oauthClient;

    /**
     * 设置登录人
     */
    @Value("${scm.default.loginName:}")
    private String defaultLoginNme;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @ApiOperation(value = "供应链外部创建内陆订单")
    @RequestMapping(value = "/createOutOrderTransport")
    String createOutOrderTransport(@RequestBody Map<String, Object> param) throws Exception {
        //appid
        String appId = MapUtil.getStr(param, "appId");
        //加密的字符串
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");
//        String signType = MapUtil.getStr(param, "signType");
        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        //查询到的私钥
        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //使用传进来的公钥解密
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));
// RSA验签
//        boolean result1 = RSAUtils.verify(jmm, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY), signType);
//      //验证签名失败
//        if (result1==false) {
//            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
//
//        }

        com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);


        InputOrderOutInlandTransportFrom form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);

        if (log.isInfoEnabled()) {
            log.info("供应链调用创建内陆订单接口：{}", JSONUtil.toJsonStr(form));
        }
        //通用参数校验
        if (form == null) {
            //  私钥加密
            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }
        //订单验证
        form.checkOrderTransportParam();

        String key = "SCM-" + form.getOrderNo();
        int limit = redisTemplate.opsForValue().increment(key, 1).intValue();
        redisTemplate.expire(key, 20, TimeUnit.SECONDS);
        if (limit > 1) {
            log.warn("创建订单失败 message=第三方订单号已存在");
            return privatekey(ApiResult.error("第三方订单号已存在"), appPrivateSecret);
        }

        ApiResult apiResult = null;
        try {
            //查询内陆订单表根据 第三方订单号去查询 订单 存不存在
            OutOrderInlandTransportVO outOrderInlandTransportVO = orderInlandTransportService.getOutOrderInlandTransportVOByThirdPartyOrderNo(form.getOrderNo());
            if (outOrderInlandTransportVO != null) {
                log.warn("创建订单失败 message=第三方订单号已存在");
                return privatekey(ApiResult.error("第三方订单号已存在"), appPrivateSecret);
            }
            apiResult = this.createOrUpdateOutOrderInlandTransport(form, outOrderInlandTransportVO, false, companyId);
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

    @ApiOperation(value = "供应链外部修改内陆订单")
    @RequestMapping(value = "/updateOutOrderTransport")
    String updateOutOrderTransport(@RequestBody Map<String, Object> param) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String appId = MapUtil.getStr(param, "appId");
        //加密的字符串
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");
        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //使用传进来的私钥解密
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));

        com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);


        InputOrderOutInlandTransportFrom form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);

        if (log.isInfoEnabled()) {
            log.info("供应链调用修改内陆订单接口：{}", JSONUtil.toJsonStr(form));
        }

        //通用参数校验
        if (form == null) {
//            return ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }

        //订单验证
        form.checkOrderTransportParam();

        OutOrderInlandTransportVO outOrderInlandTransportVO = orderInlandTransportService.getOutOrderInlandTransportVOByThirdPartyOrderNo(form.getOrderNo());
        if (outOrderInlandTransportVO == null) {
            log.warn("修改订单失败 message=根据第三方订单号查询不到中港订单信息");
            return privatekey(ApiResult.error("查询不到订单信息"), appPrivateSecret);
        }
        ApiResult orUpdateOutOrderInlandTransport = this.createOrUpdateOutOrderInlandTransport(form, outOrderInlandTransportVO, true, companyId);
        return privatekey(orUpdateOutOrderInlandTransport, appPrivateSecret);
    }


    @ApiOperation(value = "供应链外部关闭内陆订单")
    @RequestMapping(value = "/deleteOutOrderTransport")
    String deleteOutOrderTransport(@RequestBody Map<String, Object> param) throws InvalidKeySpecException, NoSuchAlgorithmException {


        String appId = MapUtil.getStr(param, "appId");
        //加密的字符串
        String rsaString = MapUtil.getStr(param, "data");
        String publicKey = MapUtil.getStr(param, "publicKey");
        ApiResult result = omsClient.findClientSecretKeyOne(appId);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用查询客户信息失败 message=" + result.getMsg());
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }
        JSONObject jsonObject = JSONUtil.parseObj(result.getData());

        Long companyId = Long.valueOf(jsonObject.getStr("customerInfoId")).longValue();

        String appPrivateSecret = jsonObject.getStr("appPrivateSecret");
//        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(jsonObject.getStr("appSecret")));
        //使用传进来的私钥解密
        String jmm = RSAUtils.publicDecrypt(rsaString, RSAUtils.getPublicKey(publicKey));

        com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);


        InputOrderOutInlandTransportFrom form = com.alibaba.fastjson.JSONObject.parseObject(jmm, InputOrderOutInlandTransportFrom.class);

        if (log.isInfoEnabled()) {
            log.info("供应链调用修改内陆订单接口：{}", JSONUtil.toJsonStr(form));
        }

        //通用参数校验
        if (form == null) {
            return privatekey(ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()), appPrivateSecret);
        }

        //订单验证
        form.checkOrderTransportParam();

        OutOrderInlandTransportVO outOrderInlandTransportVO = orderInlandTransportService.getOutOrderInlandTransportVOByThirdPartyOrderNo(form.getMainOrderNo());
        if (outOrderInlandTransportVO != null) {
            log.warn("修改订单失败 message=根据第三方订单号查询不到中港订单信息");
            return privatekey(ApiResult.error("查询不到订单信息"), appPrivateSecret);
        }


        //根据主订单单号去关闭主订单
        ApiResult apiResult = omsClient.deleteOrderInfoUpdateByIdOne(outOrderInlandTransportVO.getMainOrderNo());

        OrderInlandTransport inlandTransport = new OrderInlandTransport();
        inlandTransport.setId(outOrderInlandTransportVO.getId());
        inlandTransport.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
        inlandTransport.setUpdateTime(LocalDateTime.now());

        this.orderInlandTransportService.updateById(inlandTransport);

        return privatekey(apiResult, appPrivateSecret);
    }

    /**
     * 创建或修改内陆订单
     *
     * @param form
     * @param isEdit
     * @return
     */
    private ApiResult createOrUpdateOutOrderInlandTransport(InputOrderOutInlandTransportFrom form, OutOrderInlandTransportVO outOrderInlandTransportVO, boolean isEdit, Long companyId) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String tip = isEdit ? "修改" : "创建";
        InputOrderForm orderForm = new InputOrderForm();
        //查询客户名称 传入客户id查询客户信息
        JSONObject customerInfo = orderInlandTransportService.getCustomerInfoByLoginUserName(companyId);
        InputMainOrderIfForm mainOrderForm = new InputMainOrderIfForm();
        //主订单设置客户名称
        mainOrderForm.setCustomerName(customerInfo.getStr("name"));
        mainOrderForm.setCustomerCode(customerInfo.getStr("idCode"));
        mainOrderForm.setClassCode(OrderStatusEnum.NLYS.getCode());
        mainOrderForm.setSelectedServer(OrderStatusEnum.NLYS.getCode());
        mainOrderForm.setCreateUserType(CreateUserTypeEnum.SCM.getCode());
        mainOrderForm.setBizUid(customerInfo.getLong("ywId"));
        mainOrderForm.setBizUname(customerInfo.getStr("ywName"));

        if (isEdit) {
            mainOrderForm.setOrderId(outOrderInlandTransportVO.getMainOrderId());
            mainOrderForm.setOrderNo(outOrderInlandTransportVO.getMainOrderNo());
        }

        // 获取业务编码，这里固定了为国内陆运
        ApiResult<String> productBizIdCodeResult = omsClient.getProductBizIdCodeByName("国内陆运");
        if (productBizIdCodeResult.getCode() != HttpStatus.SC_OK) {
            log.warn("根据业务名称查询业务代码接口请求失败 message={}", productBizIdCodeResult.getMsg());
            return ApiResult.error(tip + "订单失败");
        }
        if (productBizIdCodeResult.getData() == null) {
            log.warn("找不到对应的业务编号 message={}", productBizIdCodeResult.getMsg());
            return ApiResult.error("找不到对应的业务编号");
        }
        mainOrderForm.setBizCode(productBizIdCodeResult.getData());
        mainOrderForm.setBizBelongDepart(customerInfo.getLong("departmentId"));
        mainOrderForm.setUnitAccount(customerInfo.getStr("name"));
        mainOrderForm.setUnitCode(customerInfo.getStr("idCode"));
        mainOrderForm.setOperationTime(LocalDateTime.now());
        mainOrderForm.setCmd("preSubmit");
        mainOrderForm.setOrderNo(form.getOrderNo());

        //组装内陆订单
        InputOrderInlandTransportForm inputOrderInlandTransportForm = new InputOrderInlandTransportForm();

        inputOrderInlandTransportForm.setProcessStatus(2);//流程状态
//        inputOrderInlandTransportForm.setOrderNo(form.getOrderNo());//内陆子订单编号
        inputOrderInlandTransportForm.setMainOrderNo(form.getOrderNo());//内陆主订单编号
        //状态(NL_0待接单,NL_1内陆接单,NL_1_1内陆接单驳回,NL_2内陆派车,NL_2_1内陆派车驳回,NL_3派车审核,\r\nNL_3_1派车审核不通过,NL_3_2派车审核驳回,
        // \r\nNL_4确认派车,NL_4_1确认派车驳回,NL_5车辆提货,NL_5_1车辆提货驳回,NL_6货物签收)',
//        inputOrderInlandTransportForm.setStatus("NL_1");//状态
        inputOrderInlandTransportForm.setCreateUserType(CreateUserTypeEnum.SCM.getCode());
        inputOrderInlandTransportForm.setVehicleType(form.getVehicleType());//车型
        inputOrderInlandTransportForm.setVehicleSize(form.getVehicleSize());//车型(3T 5t 8T 10T 12T 20GP 40GP 45GP..)
        inputOrderInlandTransportForm.setThirdPartyOrderNo(form.getOrderNo());
//        inputOrderInlandTransportForm.setType(form.getType()); //标识 1类型1    2类型2
        // 提货信息
        if (CollectionUtil.isNotEmpty(form.getTakeAdrForms1())) {
            List<OrderDeliveryAddress> orderTakeAdrForms1 = new ArrayList<>();
            for (InputOrderOutTakeAdrForm address : form.getTakeAdrForms1()) {
                OrderDeliveryAddress convert = ConvertUtil.convert(address, OrderDeliveryAddress.class);

                if (!StringUtils.isAnyBlank(address.getProvinceName(),
                        address.getAreaName(),
                        address.getCityName())) {
                    ApiResult<Map<String, Long>> regionCityIdMap = omsClient.getRegionCityIdMapByName(address.getProvinceName(),
                            address.getCityName(), address.getAreaName());
                    if (regionCityIdMap.getCode() != HttpStatus.SC_OK) {
                        log.warn("根据省市区名称列表获取提货信息Map接口请求失败 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("找不到对应省市区 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("找不到对应省市区");
                    }

                    convert.setProvince(regionCityIdMap.getData().get(address.getProvinceName()));
                    convert.setCity(regionCityIdMap.getData().get(address.getCityName()));
                    convert.setArea(regionCityIdMap.getData().get(address.getAreaName()));
                }

                // 提货时间  dateOfDelivery
                convert.setDeliveryDate(address.getDate());
                orderTakeAdrForms1.add(convert);
            }
            inputOrderInlandTransportForm.setPickUpAddressList(orderTakeAdrForms1);
        }

        // 送货信息
        if (CollectionUtil.isNotEmpty(form.getTakeAdrForms2())) {
            List<OrderDeliveryAddress> orderDeliveryAddress2 = new ArrayList<>();
            for (InputOrderOutTakeAdrForm address2 : form.getTakeAdrForms2()) {
                OrderDeliveryAddress convert2 = ConvertUtil.convert(address2, OrderDeliveryAddress.class);

                if (!StringUtils.isAnyBlank(address2.getProvinceName(),
                        address2.getAreaName(),
                        address2.getCityName())) {
                    ApiResult<Map<String, Long>> regionCityIdMap = omsClient.getRegionCityIdMapByName(address2.getProvinceName(),
                            address2.getCityName(), address2.getAreaName());
                    if (regionCityIdMap.getCode() != HttpStatus.SC_OK) {
                        log.warn("根据省市区名称列表获取送货信息Map接口请求失败 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("找不到对应省市区 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("找不到对应省市区");
                    }
                    convert2.setProvince(regionCityIdMap.getData().get(address2.getProvinceName()));
                    convert2.setCity(regionCityIdMap.getData().get(address2.getCityName()));
                    convert2.setArea(regionCityIdMap.getData().get(address2.getAreaName()));
                }

                // 送货时间
                convert2.setDeliveryDate(address2.getDate());
                orderDeliveryAddress2.add(convert2);
            }
            inputOrderInlandTransportForm.setOrderDeliveryAddressList(orderDeliveryAddress2);
        }

        // 操作主体
        String legalEntityIdStr = customerInfo.getStr("legalEntityIdStr");
        inputOrderInlandTransportForm.setLegalEntityId(Long.parseLong(legalEntityIdStr.split(",")[0]));
        inputOrderInlandTransportForm.setLegalName(oauthClient.getLegalNameByLegalId(inputOrderInlandTransportForm.getLegalEntityId()).getData());
        inputOrderInlandTransportForm.setDepartmentId(customerInfo.getLong("departmentId"));
        inputOrderInlandTransportForm.setUnitCode(customerInfo.getStr("idCode"));
        inputOrderInlandTransportForm.setUnitName(customerInfo.getStr("name"));

        mainOrderForm.setUnitAccount(customerInfo.getStr("name"));
        mainOrderForm.setUnitCode(customerInfo.getStr("idCode"));
        // 接单法人
        mainOrderForm.setLegalName(inputOrderInlandTransportForm.getLegalName());
        mainOrderForm.setLegalEntityId(inputOrderInlandTransportForm.getLegalEntityId());
        // 1表示需要齐全的报关数据
        mainOrderForm.setIsDataAll("");

        if (isEdit) {
            // 填充修改信息
            inputOrderInlandTransportForm.setId(outOrderInlandTransportVO.getId());
            inputOrderInlandTransportForm.setOrderNo(outOrderInlandTransportVO.getOrderNo());
        }
//        orderForm.setLoginUserName(customerInfo.getStr("createdUser"));
        orderForm.setCmd(mainOrderForm.getCmd());
        orderForm.setOrderForm(mainOrderForm);
        orderForm.setOrderInlandTransportForm(inputOrderInlandTransportForm);
        //设置固定登录人
        orderForm.setLoginUserName(defaultLoginNme);

        // 保存或修改订单消息
        return omsClient.saveOrUpdateOutMainOrderForm(orderForm);
    }

    //加密处理回调
    public String privatekey(ApiResult booleanApiResult, String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String s1 = com.alibaba.fastjson.JSONObject.toJSONString(booleanApiResult);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("data", s1);
        return RSAUtils.privateEncrypt(s1, RSAUtils.getPrivateKey(privateKey));
    }
}

