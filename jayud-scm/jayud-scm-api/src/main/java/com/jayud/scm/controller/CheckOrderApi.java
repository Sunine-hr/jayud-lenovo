package com.jayud.scm.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import com.jayud.scm.utils.DateUtil;
import com.jayud.scm.utils.RSAUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RequestMapping("/api")
@Api(tags = "提验货对外接口")

@RestController
@Slf4j
public class CheckOrderApi {

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private ICheckOrderEntryService checkOrderEntryService;

    @Autowired
    private IHubShippingDeliverService hubShippingDeliverService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private IHubShippingService hubShippingService;

    @Autowired
    private ICommodityService commodityService;

    @Value("${scmApi.url.createOutOrderTransport}")
    private String createOutOrderTransport;

    @Value("${scmApi.url.deleteShippingDeliver}")
    private String deleteShippingDeliver;

    //  出口提货走内陆车，送货走港车   提验货订车
    @ApiOperation(value = "提验货推送信息至oms内陆运输")
    @PostMapping(value = "/pushCheckOrder")
    public CommonResult pushCheckOrder(@RequestBody QueryCommonForm form) {
        CheckOrder checkOrder = this.checkOrderService.getById(form.getId());

        if(checkOrder.getShippingDeliverId() != null){
            return CommonResult.error(444,"该提验货单已调度，不能重复调度");
        }

        if(checkOrder.getCheckType() != null && checkOrder.getCheckType().equals("仓库验货")){
            return CommonResult.error(444,"验货类型为仓库验货的单不能进行订车");
        }

        if(!checkOrder.getModelType().equals(2)){
            return CommonResult.error(444,"只有出口的提验货单才能订车");
        }

        if(checkOrder.getPickAddress() == null){
            return CommonResult.error(444,"提货地址为空不能订车");
        }

        //新建调度单  出库单绑定调度策略
        HubShippingDeliver hubShippingDeliver = new HubShippingDeliver();
        hubShippingDeliver.setModelType(checkOrder.getModelType());
        hubShippingDeliver.setTransType("陆运");
        hubShippingDeliver.setDeliverType(2);
        hubShippingDeliver.setHubName("深圳仓");
        hubShippingDeliver.setPushOms(1);
        hubShippingDeliver.setDeliverNo(commodityService.getOrderNo(NoCodeEnum.HUB_SHIPPING_DELIVER.getCode(),LocalDateTime.now()));


        HubToInlandTransportVO hubToInlandTransportVO = new HubToInlandTransportVO();
        hubToInlandTransportVO.setOrderNo(hubShippingDeliver.getDeliverNo());
        hubToInlandTransportVO.setVehicleType("1");
        hubToInlandTransportVO.setVehicleSize(checkOrder.getRemark());

        if(checkOrder.getTruckModel().equals("陆运")){
            List<AddAddressForm> takeAdrForms1 = new ArrayList<>();
            List<AddAddressForm> takeAdrForms2 = new ArrayList<>();
            BDataDicEntry dicEntryByDicCode = ibDataDicEntryService.getBDataDicEntryByDicCode("1049", "深圳仓");

            List<CheckOrderEntry> checkOrderEntries = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrder.getId().longValue());

            for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {

                AddAddressForm addAddressForm = new AddAddressForm();
                AddAddressForm addAddressForm1 = new AddAddressForm();
                Integer pieceAmount = 0;
                Double weight = 0.0;
                Double volume = 0.0;
                Integer pallets = 0;

                pieceAmount = pieceAmount + (checkOrderEntry.getPackages()!=null ?checkOrderEntry.getPackages():new BigDecimal(0)).intValue();
                weight = weight + (checkOrderEntry.getGw()!=null ?checkOrderEntry.getGw():new BigDecimal(0)).doubleValue();
                volume = volume + (checkOrderEntry.getCbm()!=null ?checkOrderEntry.getCbm():new BigDecimal(0)).doubleValue();
                pallets = pallets + (checkOrderEntry.getPallets()!=null ?checkOrderEntry.getPallets():new BigDecimal(0)).intValue();

                addAddressForm.setPieceAmount(pallets);
                addAddressForm.setWeight(weight);
                addAddressForm.setBulkCargoAmount(pieceAmount);
                addAddressForm.setVolume(volume);
                addAddressForm.setDate(DateUtil.localDateTimeToString(checkOrder.getToWarehouseTime() == null ? LocalDateTime.now():checkOrder.getToWarehouseTime(),"yyyy-MM-dd HH:mm:ss"));
                addAddressForm1.setPieceAmount(pallets);
                addAddressForm1.setDate(DateUtil.localDateTimeToString((checkOrder.getCheckDateTime() == null ? LocalDateTime.now():checkOrder.getCheckDateTime()),"yyyy-MM-dd HH:mm:ss"));
                addAddressForm1.setWeight(weight);
                addAddressForm1.setBulkCargoAmount(pieceAmount);
                addAddressForm1.setVolume(volume);
                addAddressForm.setCityName(dicEntryByDicCode.getReserved3());
                if(dicEntryByDicCode.getReserved4() != null){
                    addAddressForm.setAreaName(dicEntryByDicCode.getReserved4());
                }
                addAddressForm.setProvinceName(dicEntryByDicCode.getReserved2());
                addAddressForm.setAddress(dicEntryByDicCode.getReserved5());
                String[] s = dicEntryByDicCode.getReserved1().split(" ");
                addAddressForm.setContacts(s[0]);
                addAddressForm.setPhone(s[1]);
                addAddressForm.setGoodsDesc(checkOrderEntry.getItemName());

                addAddressForm1.setGoodsDesc(checkOrderEntry.getItemName());
                addAddressForm1.setPhone(checkOrder.getPickUpTel());
                addAddressForm1.setContacts(checkOrder.getPickUpUser());
                addAddressForm1.setAddress(checkOrder.getPickAddress());
                takeAdrForms1.add(addAddressForm);
                takeAdrForms2.add(addAddressForm1);
            }
            hubToInlandTransportVO.setTakeAdrForms1(takeAdrForms2);
            hubToInlandTransportVO.setTakeAdrForms2(takeAdrForms1);
        }else{
            List<AddAddressForm> takeAdrForms1 = new ArrayList<>();
            List<AddAddressForm> takeAdrForms2 = new ArrayList<>();
            List<CheckOrderEntry> checkOrderEntries = checkOrderEntryService.getCheckOrderEntryByCheckOrderId(checkOrder.getId().longValue());

            for (CheckOrderEntry checkOrderEntry : checkOrderEntries) {

                AddAddressForm addAddressForm = new AddAddressForm();
                AddAddressForm addAddressForm1 = new AddAddressForm();
                Integer pieceAmount = 0;
                Double weight = 0.0;
                Double volume = 0.0;
                Integer pallets = 0;

                pieceAmount = pieceAmount + (checkOrderEntry.getPackages()!=null ?checkOrderEntry.getPackages():new BigDecimal(0)).intValue();
                weight = weight + (checkOrderEntry.getGw()!=null ?checkOrderEntry.getGw():new BigDecimal(0)).doubleValue();
                volume = volume + (checkOrderEntry.getCbm()!=null ?checkOrderEntry.getCbm():new BigDecimal(0)).doubleValue();
                pallets = pallets + (checkOrderEntry.getPallets()!=null ?checkOrderEntry.getPallets():new BigDecimal(0)).intValue();

                addAddressForm.setPieceAmount(pallets);
                addAddressForm.setWeight(weight);
                addAddressForm.setBulkCargoAmount(pieceAmount);
                addAddressForm.setVolume(volume);
                addAddressForm.setDate(DateUtil.localDateTimeToString(checkOrder.getToWarehouseTime() == null ? LocalDateTime.now():checkOrder.getToWarehouseTime(),"yyyy-MM-dd HH:mm:ss"));
                addAddressForm1.setPieceAmount(pallets);
                addAddressForm1.setDate(DateUtil.localDateTimeToString((checkOrder.getCheckDateTime() == null ? LocalDateTime.now():checkOrder.getCheckDateTime()),"yyyy-MM-dd HH:mm:ss"));
                addAddressForm1.setWeight(weight);
                addAddressForm1.setBulkCargoAmount(pieceAmount);
                addAddressForm1.setVolume(volume);
                addAddressForm.setAddress(checkOrder.getWhAddress());
                addAddressForm.setContacts(checkOrder.getWhName());
                addAddressForm.setPhone(checkOrder.getWhTel());
                addAddressForm.setGoodsDesc(checkOrderEntry.getItemName());

                addAddressForm1.setGoodsDesc(checkOrderEntry.getItemName());
                addAddressForm1.setPhone(checkOrder.getPickUpTel());
                addAddressForm1.setContacts(checkOrder.getPickUpUser());
                addAddressForm1.setAddress(checkOrder.getPickAddress());
                takeAdrForms1.add(addAddressForm);
                takeAdrForms2.add(addAddressForm1);
            }
            hubToInlandTransportVO.setTakeAdrForms1(takeAdrForms2);
            hubToInlandTransportVO.setTakeAdrForms2(takeAdrForms1);
        }


        System.out.println("请求参数："+hubToInlandTransportVO);

        //加密参数
//        String s1 = null;
//        try {
//            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(hubToInlandTransportVO), RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
//        jsonObject.put("appid",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);
        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(hubToInlandTransportVO));
//        return encryptedData;

        //请求
        String feedback = HttpRequest
                .post(createOutOrderTransport)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(encryptedData))
                .execute().body();
        log.warn("pushCheckOrder:"+feedback);

        //解密数据
        String s = null;
        try {
            s = RSAUtils.publicDecrypt(feedback, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //获取token
        Map map1 = JSONUtil.toBean(s, Map.class);
        if(map1.get("code").equals(200)){

            HubShippingDeliver hubShippingDeliver1 = this.hubShippingDeliverService.saveHubShippingDeliver(hubShippingDeliver);

            if(hubShippingDeliver1 == null){
                log.warn("调度策略单创建失败");
            }

            checkOrder.setShippingDeliverId(hubShippingDeliver.getId());
            boolean result = this.checkOrderService.saveOrUpdate(checkOrder);
            if(result){
                log.warn("提验货单调度成功");
            }

            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)(map1.get("msg")!=null?map1.get("msg"):""));
    }


    @ApiOperation(value = "订车撤回")
    @PostMapping(value = "/deleteShippingDeliver")
    public CommonResult deleteShippingDeliver(@RequestBody QueryCommonForm form) {
        HubShippingDeliver hubShippingDeliver = this.hubShippingDeliverService.getById(form.getId());

        if(hubShippingDeliver.getPushOms().equals(0)){
            return CommonResult.error(444,"只有推送至oms单才能进行订车撤回");
        }

        List<CheckOrder> checkOrders = this.checkOrderService.getCheckOrderByDeliverId(hubShippingDeliver.getId());

        List<HubShipping> hubShippings = hubShippingService.getHubShippingByDeliverId(hubShippingDeliver.getId());

        hubShippingDeliver.setVoided(1);
        hubShippingDeliver.setVoidedByDtm(LocalDateTime.now());


        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderNo",hubShippingDeliver.getDeliverNo());
        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(map2));
//        return encryptedData;

        //请求
        String feedback = HttpRequest
                .post(deleteShippingDeliver)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(encryptedData))
                .execute().body();
        log.warn("deleteShippingDeliver:"+feedback);

        //解密数据
        String s = null;
        try {
            s = RSAUtils.publicDecrypt(feedback, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        Map map1 = JSONUtil.toBean(s, Map.class);
        if(map1.get("code").equals(200)){

            boolean result1 = this.hubShippingDeliverService.saveOrUpdate(hubShippingDeliver);
            if(!result1){
                return CommonResult.error(444,"订车撤回失败");
            }

            if(CollectionUtil.isNotEmpty(checkOrders)){
                for (CheckOrder checkOrder : checkOrders) {
                    checkOrder.setShippingDeliverId(null);
                }
                boolean result = this.checkOrderService.saveOrUpdateBatch(checkOrders);
                if(result){
                    log.warn("提验货单取消调度成功");
                }
            }

            if(CollectionUtil.isNotEmpty(hubShippings)){
                for (HubShipping hubShipping : hubShippings) {
                    hubShipping.setDeliverId(null);
                    hubShipping.setDeliverNo(null);
                }
                boolean result = this.hubShippingService.saveOrUpdateBatch(hubShippings);
                if(result){
                    log.warn("出库单取消调度成功");
                }
            }

            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)(map1.get("msg")!=null?map1.get("msg"):""));
    }

    @ApiOperation(value = "删除调度策略")
    @PostMapping(value = "/deleteShippingDeliverByOms")
    public String deleteShippingDeliverByOms(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map1 = JSONUtil.toBean(s, Map.class);

        String orderNo = MapUtil.getStr(map1, "orderNo");
        JSONObject jsonObject = null;
        if(StringUtils.isEmpty(orderNo)){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"订单编号为空")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号为空");
        }
        QueryWrapper<HubShippingDeliver> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubShippingDeliver::getDeliverNo,orderNo);
        queryWrapper.lambda().eq(HubShippingDeliver::getVoided,0);
        HubShippingDeliver hubShippingDeliver = this.hubShippingDeliverService.getOne(queryWrapper);
        if(hubShippingDeliver == null){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"订单编号不存在")));
            return JSONUtil.toJsonStr(jsonObject);
//            return CommonResult.error(444,"港车编号不存在");
        }

        List<CheckOrder> checkOrders = this.checkOrderService.getCheckOrderByDeliverId(hubShippingDeliver.getId());

        List<HubShipping> hubShippings = hubShippingService.getHubShippingByDeliverId(hubShippingDeliver.getId());

        hubShippingDeliver.setVoided(1);
        hubShippingDeliver.setVoidedByDtm(LocalDateTime.now());
        boolean result1 = this.hubShippingDeliverService.saveOrUpdate(hubShippingDeliver);
        if(!result1){
            jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"删除调度策略失败")));
            return JSONUtil.toJsonStr(jsonObject);
        }

        if(CollectionUtil.isNotEmpty(checkOrders)){
            for (CheckOrder checkOrder : checkOrders) {
                checkOrder.setShippingDeliverId(null);
            }
            boolean result = this.checkOrderService.saveOrUpdateBatch(checkOrders);
            if(result){
                log.warn("提验货单取消调度成功");
            }
        }

        if(CollectionUtil.isNotEmpty(hubShippings)){
            for (HubShipping hubShipping : hubShippings) {
                hubShipping.setDeliverId(null);
                hubShipping.setDeliverNo(null);
            }
            boolean result = this.hubShippingService.saveOrUpdateBatch(hubShippings);
            if(result){
                log.warn("出库单取消调度成功");
            }
        }
        jsonObject = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(jsonObject);

    }



    @ApiOperation(value = "接受oms内陆运输返回的信息")
    @PostMapping(value = "/checkOrder/getDataByOms")
    public String getDataByOms(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("提验货参数接收成功："+s);
        Gson gson = new Gson();
        AddHubShippingDeliverForm form = gson.fromJson(s, AddHubShippingDeliverForm.class);

        String s2 = form.checkParam();
        if(s2 != null){
            JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,s2)));
            return JSONUtil.toJsonStr(encryptedData);
        }

        QueryWrapper<HubShippingDeliver> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(HubShippingDeliver::getDeliverNo,form.getOrderNo());
        queryWrapper.lambda().eq(HubShippingDeliver::getVoided,0);
        HubShippingDeliver hubShippingDeliver2 = this.hubShippingDeliverService.getOne(queryWrapper);

        HubShippingDeliver hubShippingDeliver = ConvertUtil.convert(form, HubShippingDeliver.class);
//        hubShippingDeliver.setModelType(checkOrder.getModelType());
//        hubShippingDeliver.setTransType("陆运");
//        hubShippingDeliver.setDeliverType(2);
//        hubShippingDeliver.setHubName("深圳仓");
        hubShippingDeliver.setId(hubShippingDeliver2.getId());
        hubShippingDeliver.setCheckStateFlag("Y");
        hubShippingDeliver.setFLevel(1);
        hubShippingDeliver.setFStep(1);
        hubShippingDeliver.setStateFlag(2);
        boolean result = this.hubShippingDeliverService.saveOrUpdate(hubShippingDeliver);
        if(!result){
            log.warn("调度策略单修改失败");
            JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.error(444,"调度策略单修改失败")));
            return JSONUtil.toJsonStr(encryptedData);
        }

        List<HubShipping> hubShippings = hubShippingService.getHubShippingByDeliverId(hubShippingDeliver.getId());
        if(CollectionUtil.isNotEmpty(hubShippings)){
            for (HubShipping hubShipping : hubShippings) {
                hubShipping.setStateFlag(2);
            }
            boolean result1 = this.hubShippingService.saveOrUpdateBatch(hubShippings);
            if(result1){
                log.warn("出库状态修改成功");
            }
        }

//        checkOrder.setShippingDeliverId(hubShippingDeliver.getId());
//        boolean result = this.checkOrderService.saveOrUpdate(checkOrder);
//        if(result){
//            log.warn("提验货单调度成功");
//        }

        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(encryptedData);
    }

    @ApiOperation(value = "接受oms内陆运输返回的错误信息")
    @PostMapping(value = "/checkOrder/getErrorInformation")
    public JSONObject getErrorInformation(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("出库错误信息接收成功：" + s);
        Map map1 = JSONUtil.toBean(s, Map.class);

        QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CheckOrder::getCheckNo,map1.get("checkNo"));
        queryWrapper.lambda().eq(CheckOrder::getVoided,0);
        CheckOrder checkOrder = this.checkOrderService.getOne(queryWrapper);

        Integer deliverId = checkOrder.getShippingDeliverId();
        checkOrder.setBookCarRemark((String)map.get("msg"));
        checkOrder.setShippingDeliverId(null);
        boolean result = this.checkOrderService.saveErrorInformation(checkOrder);

        if(result){
            log.warn("提验货单去绑配送车辆成功");
            HubShippingDeliver hubShippingDeliver = new HubShippingDeliver();
            hubShippingDeliver.setId(deliverId);
            hubShippingDeliver.setVoided(1);
            hubShippingDeliver.setVoidedByDtm(LocalDateTime.now());
            boolean result1 = this.hubShippingDeliverService.saveOrUpdate(hubShippingDeliver);
            if(result1){
                log.warn("删除调度策略成功");
            }
        }


//        String s1 = null;
//        try {
//            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(CommonResult.success()) , RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
//        jsonObject.put("appid",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);
//
//        return jsonObject;
        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return encryptedData;
    }


}
