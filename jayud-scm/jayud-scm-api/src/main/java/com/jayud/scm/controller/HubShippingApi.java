package com.jayud.scm.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAddressForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.NoCodeEnum;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.HubToInlandTransportVO;
import com.jayud.scm.service.*;
import com.jayud.scm.utils.DateUtil;
import com.jayud.scm.utils.RSAUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@Api(tags = "出库对外接口")

@RestController
@Slf4j
public class HubShippingApi {

    @Autowired
    private IHubShippingService hubShippingService;

    @Autowired
    private IHubShippingDeliverService hubShippingDeliverService;

    @Autowired
    private IBDataDicEntryService ibDataDicEntryService;

    @Autowired
    private IHubShippingEntryService hubShippingEntryService;

    @Autowired
    private ICommodityService commodityService;

    @Value("${scmApi.url.createOutOrderTransport}")
    private String createOutOrderTransport;



    //  进口提货走港车，送货走内陆车   出库单订车
    @ApiOperation(value = "出库单推送信息至oms内陆运输")
    @PostMapping(value = "/pushHubShipping")
    public CommonResult pushHubShipping(@RequestBody QueryCommonForm form) {
        HubShipping hubShipping = this.hubShippingService.getById(form.getId());

        if(hubShipping.getDeliverId() != null){
            return CommonResult.error(444,"该出库单已调度，不能重复订车");
        }

        if(hubShipping.getDeliverType().equals("配送")){
            return CommonResult.error(444,"发货类型为配送的出库单才能订车");
        }

        if(hubShipping.getWhAddress() == null){
            return CommonResult.error(444,"交货地址为空不能订车");
        }

        if(!hubShipping.getModelType().equals(1)){
            return CommonResult.error(444,"只有进口的出库单才能订车");
        }

        //新建调度单  出库单绑定调度策略
        HubShippingDeliver shippingDeliver = new HubShippingDeliver();
        shippingDeliver.setModelType(hubShipping.getModelType());
        shippingDeliver.setTransType("陆运");
        shippingDeliver.setDeliverType(2);
        shippingDeliver.setHubName("深圳仓");
        shippingDeliver.setPushOms(1);
        shippingDeliver.setDeliverNo(commodityService.getOrderNo(NoCodeEnum.HUB_SHIPPING_DELIVER.getCode(),LocalDateTime.now()));


        HubToInlandTransportVO hubToInlandTransportVO = new HubToInlandTransportVO();
        hubToInlandTransportVO.setOrderNo(shippingDeliver.getDeliverNo());
        hubToInlandTransportVO.setVehicleType("1");
        hubToInlandTransportVO.setVehicleSize(hubShipping.getRemark());

        List<AddAddressForm> takeAdrForms1 = new ArrayList<>();
        List<AddAddressForm> takeAdrForms2 = new ArrayList<>();
        BDataDicEntry dicEntryByDicCode = ibDataDicEntryService.getBDataDicEntryByDicCode("1049", "深圳仓");

        List<HubShippingEntry> shippingEntryByShippingId = hubShippingEntryService.getShippingEntryByShippingId(hubShipping.getId().longValue());

        for (HubShippingEntry hubShippingEntry : shippingEntryByShippingId) {

            AddAddressForm addAddressForm = new AddAddressForm();
            AddAddressForm addAddressForm1 = new AddAddressForm();
            Integer pieceAmount = 0;
            Double weight = 0.0;
            Double volume = 0.0;
            Integer pallets = 0;

            pieceAmount = pieceAmount + (hubShippingEntry.getPackages()!=null ?hubShippingEntry.getPackages():new BigDecimal(0)).intValue();
            weight = weight + (hubShippingEntry.getGw()!=null ?hubShippingEntry.getGw():new BigDecimal(0)).doubleValue();
            volume = volume + (hubShippingEntry.getCbm()!=null ?hubShippingEntry.getCbm():new BigDecimal(0)).doubleValue();
            pallets = pallets + (hubShippingEntry.getPallets()!=null ?hubShippingEntry.getPallets():new BigDecimal(0)).intValue();

            addAddressForm.setPieceAmount(pallets);
            addAddressForm.setWeight(weight);
            addAddressForm.setVolume(volume);
            addAddressForm.setBulkCargoAmount(pieceAmount);
            addAddressForm.setDate(DateUtil.localDateTimeToString((hubShipping.getShippingDate() == null ? LocalDateTime.now():hubShipping.getShippingDate()),"yyyy-MM-dd HH:mm:ss"));
            addAddressForm1.setPieceAmount(pallets);
            addAddressForm1.setWeight(weight);
            addAddressForm1.setVolume(volume);
            addAddressForm1.setBulkCargoAmount(pieceAmount);
            addAddressForm1.setDate(DateUtil.localDateTimeToString((hubShipping.getDeliveryDate() == null ? LocalDateTime.now():hubShipping.getDeliveryDate()),"yyyy-MM-dd HH:mm:ss"));
            addAddressForm.setCityName(dicEntryByDicCode.getReserved3());
            if(dicEntryByDicCode.getReserved4() != null){
                addAddressForm.setAreaName(dicEntryByDicCode.getReserved4());
            }
            addAddressForm.setProvinceName(dicEntryByDicCode.getReserved2());
            addAddressForm.setAddress(dicEntryByDicCode.getReserved5());
            String[] s = dicEntryByDicCode.getReserved1().split(" ");
            addAddressForm.setContacts(s[0]);
            addAddressForm.setPhone(s[1]);
            addAddressForm.setGoodsDesc(hubShippingEntry.getItemName());

            addAddressForm1.setGoodsDesc(hubShippingEntry.getItemName());
            addAddressForm1.setPhone(hubShipping.getWhTel());
            addAddressForm1.setContacts(hubShipping.getWhName());
            addAddressForm1.setAddress(hubShipping.getWhAddress());
            takeAdrForms1.add(addAddressForm);
            takeAdrForms2.add(addAddressForm1);
        }
        hubToInlandTransportVO.setTakeAdrForms1(takeAdrForms1);
        hubToInlandTransportVO.setTakeAdrForms2(takeAdrForms2);

//        System.out.println("请求参数："+hubToInlandTransportVO);

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
//        jsonObject.put("appId",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);
        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(hubToInlandTransportVO));

//        System.out.println("加密后的参数："+encryptedData);


        //请求
        String feedback = HttpRequest
                .post(createOutOrderTransport)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(JSONUtil.toJsonStr(encryptedData))
                .execute().body();
        log.warn("pushHubShipping:"+feedback);

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

            HubShippingDeliver hubShippingDeliver1 = this.hubShippingDeliverService.saveHubShippingDeliver(shippingDeliver);
            if(hubShippingDeliver1 == null){
                log.warn("调度策略单创建失败");
            }

            hubShipping.setStateFlag(1);
            hubShipping.setDeliverId(hubShippingDeliver1.getId());
            hubShipping.setDeliverNo(hubShippingDeliver1.getDeliverNo());
            boolean result = this.hubShippingService.saveOrUpdate(hubShipping);
            if(result){
                log.warn("出库单调度成功");
            }

            return CommonResult.success();
        }
        return CommonResult.error((Integer)map1.get("code"),(String)(map1.get("msg")!=null?map1.get("msg"):""));
    }



//    @ApiOperation(value = "接受oms内陆运输返回的信息")
//    @PostMapping(value = "/hubShipping/getDataByOms")
//    public JSONObject getDataByOms(@RequestBody Map<String,Object> map) {
//
//        String data = MapUtil.getStr(map, "data");
//
//        String s = null;
//        try {
//            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        log.warn("出库参数接收成功："+s);
//
//        Gson gson = new Gson();
//        AddHubShippingDeliverForm form = gson.fromJson(s, AddHubShippingDeliverForm.class);
//
////        QueryWrapper<HubShipping> queryWrapper = new QueryWrapper();
////        queryWrapper.lambda().eq(HubShipping::getShippingNo,form.getOrderNo());
////        queryWrapper.lambda().eq(HubShipping::getVoided,0);
////        HubShipping hubShipping = this.hubShippingService.getOne(queryWrapper);
//
//        QueryWrapper<HubShippingDeliver> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(HubShippingDeliver::getDeliverNo,form.getOrderNo());
//        queryWrapper.lambda().eq(HubShippingDeliver::getVoided,0);
//        HubShipping hubShipping = this.hubShippingService.getOne(q);
//
//        HubShippingDeliver shippingDeliver = ConvertUtil.convert(form, HubShippingDeliver.class);
////        shippingDeliver.setModelType(hubShipping.getModelType());
////        shippingDeliver.setTransType("陆运");
////        shippingDeliver.setDeliverType(2);
////        shippingDeliver.setHubName("深圳仓");
//        shippingDeliver.setCheckStateFlag("Y");
//        shippingDeliver.setFLevel(1);
//        shippingDeliver.setFStep(1);
//        shippingDeliver.setStateFlag(2);
//        HubShippingDeliver hubShippingDeliver1 = this.hubShippingDeliverService.saveHubShippingDeliver(shippingDeliver);
//        if(hubShippingDeliver1 == null){
//            log.warn("调度策略单创建失败");
//        }
//
//        hubShipping.setStateFlag(2);
//        hubShipping.setDeliverId(hubShippingDeliver1.getId());
//        hubShipping.setDeliverNo(hubShippingDeliver1.getDeliverNo());
//        boolean result = this.hubShippingService.saveOrUpdate(hubShipping);
//        if(result){
//            log.warn("出库单调度成功");
//        }
//
////        String s1 = null;
////        try {
////            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(CommonResult.success()) , RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
////        jsonObject.put("appid",RSAUtils.APP_ID);
////        jsonObject.put("data",s1);
////
////        return jsonObject;
//        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
//        return encryptedData;
//    }

    @ApiOperation(value = "接受oms内陆运输返回的错误信息")
    @PostMapping(value = "/hubShipping/getErrorInformation")
    public String getErrorInformation(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("出库错误信息接收成功：" + s);
        Map map1 = JSONUtil.toBean(s, Map.class);

        QueryWrapper<HubShipping> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HubShipping::getShippingNo,map1.get("shippingNo"));
        queryWrapper.lambda().eq(HubShipping::getVoided,0);
        HubShipping hubShipping = this.hubShippingService.getOne(queryWrapper);

        Integer deliverId = hubShipping.getDeliverId();
        hubShipping.setBookCarRemark((String)map.get("msg"));
        hubShipping.setDeliverId(null);
        hubShipping.setDeliverNo(null);
        boolean result = this.hubShippingService.saveErrorInformation(hubShipping);
        if(result){
            log.warn("出库单去绑配送车辆成功");
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
//            s1 = RSAUtils.privateEncrypt(JSONUtil.toJsonStr(CommonResult.success()), RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("publicKey",RSAUtils.PUBLIC_KEY);
//        jsonObject.put("appid",RSAUtils.APP_ID);
//        jsonObject.put("data",s1);
        JSONObject encryptedData = RSAUtils.getEncryptedData(JSONUtil.toJsonStr(CommonResult.success()));
        return JSONUtil.toJsonStr(encryptedData);

    }


    public static void main(String[] args) throws Exception {

        JSONObject s = new JSONObject();
        s.put("orderNo", "EX2111000005");
        s.put("driverName", "司机姓名");
        s.put("driverTel", "司机电话");
        s.put("idCode", "证件号码");
        s.put("truckNo", "车牌");
        s.put("deliverName", "配送人 默认司机");
        s.put("deliverTime", "2021-11-22 11:15:30");

//        s.put("orderNo","EX2111000005");
//        s.put("msg","订车订单已删除");

        System.out.println(s);
        String s2 = s.toJSONString();
        System.out.println("s2:"+s2);
        String s1 = RSAUtils.privateEncrypt(s2, RSAUtils.getPrivateKey(RSAUtils.PRIVATE_KEY));
        System.out.println("s1:"+s1);
    }


}
