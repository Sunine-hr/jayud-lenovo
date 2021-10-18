package com.jayud.tms.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.aop.annotations.RepeatSubmitLimit;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
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
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 外部中港订单接口，该接口由供应商系统发起调用
 */
@RestController
@Api(tags = "外部中港订单接口")
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

    @ApiOperation(value = "供应链外部创建中港订单")
    @RequestMapping(value = "/createOutOrderTransport")
    ApiResult createOutOrderTransport(@RequestBody InputOrderOutTransportForm form) {
        if (log.isInfoEnabled()) {
            log.info("供应链调用创建中港订单接口：{}", JSONUtil.toJsonStr(form));
        }
        //通用参数校验
        if (form == null) {
            return ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        //订单验证
        form.checkOrderTransportParam();

        String key = "SCM-" + form.getOrderNo();
        int limit = redisTemplate.opsForValue().increment(key, 1).intValue();
        redisTemplate.expire(key, 20, TimeUnit.SECONDS);
        if (limit > 1) {
            log.warn("创建订单失败 message=第三方订单号已存在");
            return ApiResult.error("第三方订单号已存在");
        }

        ApiResult apiResult = null;
        try {
            OutOrderTransportVO outOrderTransportVO = orderTransportService.getOutOrderTransportVOByThirdPartyOrderNo(form.getOrderNo());
            if (outOrderTransportVO != null) {
                log.warn("创建订单失败 message=第三方订单号已存在");
                return ApiResult.error("第三方订单号已存在");
            }
            apiResult = this.createOrUpdateOutOrderTransport(form, outOrderTransportVO, false);
        } catch (Exception e) {
            redisUtils.delete(key);
            throw e;
        } finally {
            if (apiResult != null && apiResult.getCode() != HttpStatus.SC_OK) {
                redisUtils.delete(key);
            }
        }
        return apiResult;
    }

    @ApiOperation(value = "供应链外部修改中港订单")
    @RequestMapping(value = "/updateOutOrderTransport")
    ApiResult updateOutOrderTransport(@RequestBody InputOrderOutTransportForm form) {
        if (log.isInfoEnabled()) {
            log.info("供应链调用修改中港订单接口：{}", JSONUtil.toJsonStr(form));
        }

        //通用参数校验
        if (form == null) {
            return ApiResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }

        //订单验证
        form.checkOrderTransportParam();

        OutOrderTransportVO outOrderTransportVO = orderTransportService.getOutOrderTransportVOByThirdPartyOrderNo(form.getOrderNo());
        if (outOrderTransportVO == null) {
            log.warn("修改订单失败 message=根据第三方订单号查询不到中港订单信息");
            return ApiResult.error("查询不到订单信息");
        }

        return this.createOrUpdateOutOrderTransport(form, outOrderTransportVO, true);
    }

    /**
     * 创建或修改中港订单
     * @param form
     * @param isEdit
     * @return
     */
    private ApiResult createOrUpdateOutOrderTransport(InputOrderOutTransportForm form, OutOrderTransportVO outOrderTransportVO, boolean isEdit){
        String tip = isEdit ? "修改": "创建";
        InputOrderForm orderForm = new InputOrderForm();
        //查询客户名称
        JSONObject customerInfo = orderTransportService.getCustomerInfoByLoginUserName();
        InputMainOrderForm mainOrderForm = new InputMainOrderForm();
        //主订单设置客户名称
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

        // 获取业务编码，这里固定了为跨境陆运
        ApiResult<String> productBizIdCodeResult = omsClient.getProductBizIdCodeByName("跨境陆运");
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
        mainOrderForm.setCmd("submit");
        mainOrderForm.setReferenceNo(form.getOrderNo());

        //组装中港订单
        InputOrderTransportForm inputOrderTransportForm = new InputOrderTransportForm();
        //获取海关代码
        ApiResult portResult = this.omsClient.getPortCodeByName(form.getPortName());
        if (portResult.getCode() != HttpStatus.SC_OK) {
            log.warn("根据海关名称查询海关代码接口请求失败 message={}", portResult.getMsg());
            return ApiResult.error(tip + "订单失败");
        }
        if (portResult.getData() == null) {
            log.warn("找不到对应的通关口岸 message={}", portResult.getMsg());
            return ApiResult.error("找不到对应的通关口岸");
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
        // 中转仓库
        ApiResult<Long> warehouseIdResult = omsClient.getWarehouseIdByName(form.getWarehouseInfo());
        if (warehouseIdResult.getCode() != HttpStatus.SC_OK) {
            log.warn("根据仓库名称查询中转仓库ID接口请求失败 message={}", warehouseIdResult.getMsg());
            return ApiResult.error(tip + "订单失败");
        }
        if (warehouseIdResult.getData() == null) {
            log.warn("找不到对应的仓库 message={}", warehouseIdResult.getMsg());
            return ApiResult.error("找不到对应的中转仓库");
        }
        inputOrderTransportForm.setWarehouseInfoId(warehouseIdResult.getData());

        LocalDateTime truckDate = LocalDateTime.parse(form.getTruckDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        LocalDateTime receivingDate = truckDate.withHour(18).withMinute(0).withSecond(0);

        // 提货信息
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
                        log.warn("根据省市区名称列表获取提货信息Map接口请求失败 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("找不到对应省市区 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("找不到对应省市区");
                    }

                    inputOrderTakeAdrForm.setProvince(regionCityIdMap.getData().get(orderTakeAdrForm1.getProvinceName()));
                    inputOrderTakeAdrForm.setCity(regionCityIdMap.getData().get(orderTakeAdrForm1.getCityName()));
                    inputOrderTakeAdrForm.setArea(regionCityIdMap.getData().get(orderTakeAdrForm1.getAreaName()));
                }

                // 提货时间
                inputOrderTakeAdrForm.setTakeTimeStr(truckDate);
                orderTakeAdrForms1.add(inputOrderTakeAdrForm);
            }
            inputOrderTransportForm.setTakeAdrForms1(orderTakeAdrForms1);
        }

        // 送货信息
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
                        log.warn("根据省市区名称列表获取送货信息Map接口请求失败 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error(regionCityIdMap.getMsg());
                    }
                    if (regionCityIdMap.getData() == null) {
                        log.warn("找不到对应省市区 message={}", regionCityIdMap.getMsg());
                        return ApiResult.error("找不到对应省市区");
                    }
                    inputOrderTakeAdrForm.setProvince(regionCityIdMap.getData().get(orderTakeAdrForm2.getProvinceName()));
                    inputOrderTakeAdrForm.setCity(regionCityIdMap.getData().get(orderTakeAdrForm2.getCityName()));
                    inputOrderTakeAdrForm.setArea(regionCityIdMap.getData().get(orderTakeAdrForm2.getAreaName()));
                }

                // 送货时间
                inputOrderTakeAdrForm.setTakeTimeStr(receivingDate);
                orderTakeAdrForms2.add(inputOrderTakeAdrForm);
            }
            inputOrderTransportForm.setTakeAdrForms2(orderTakeAdrForms2);
        }

        // 操作主体
        String legalEntityIdStr = customerInfo.getStr("legalEntityIdStr");
        inputOrderTransportForm.setLegalEntityId(Long.parseLong(legalEntityIdStr.split(",")[0]));
        inputOrderTransportForm.setLegalName(oauthClient.getLegalNameByLegalId(inputOrderTransportForm.getLegalEntityId()).getData());
        inputOrderTransportForm.setDepartmentId(customerInfo.getLong("departmentId").intValue());
        // 接单法人
        mainOrderForm.setLegalName(inputOrderTransportForm.getLegalName());
        mainOrderForm.setLegalEntityId(inputOrderTransportForm.getLegalEntityId());
        // 非报关的订单都是资料齐全
        mainOrderForm.setIsDataAll("1");

        if (isEdit) {
            // 填充修改信息
            inputOrderTransportForm.setId(outOrderTransportVO.getId());
            inputOrderTransportForm.setOrderNo(outOrderTransportVO.getOrderNo());
        }

        orderForm.setCmd(mainOrderForm.getCmd());
        orderForm.setOrderForm(mainOrderForm);
        orderForm.setOrderTransportForm(inputOrderTransportForm);

        // 保存或修改订单消息
        return omsClient.saveOrUpdateOutMainOrderForm(orderForm);
    }

}

