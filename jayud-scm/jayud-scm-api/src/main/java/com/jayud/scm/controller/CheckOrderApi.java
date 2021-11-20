package com.jayud.scm.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.*;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.po.*;
import com.jayud.scm.model.vo.*;
import com.jayud.scm.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private IHubShippingDeliverService hubShippingDeliverService;

    //  出口提货走内陆车，送货走港车   提验货订车
    @ApiOperation(value = "提验货推送信息至oms内陆运输")
    @PostMapping(value = "/pushCheckOrder")
    public CommonResult pushCheckOrder(@RequestBody QueryCommonForm form) {

        return CommonResult.success();
    }



    @ApiOperation(value = "接受oms内陆运输返回的信息")
    @PostMapping(value = "/checkOrder/getDataByOms")
    public void getDataByOms(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("提验货参数接收成功："+s);

        AddHubShippingDeliverForm form = JSONUtil.toBean(s, AddHubShippingDeliverForm.class);

        QueryWrapper<CheckOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CheckOrder::getCheckNo,form.getCheckNo());
        queryWrapper.lambda().eq(CheckOrder::getVoided,0);
        CheckOrder checkOrder = this.checkOrderService.getOne(queryWrapper);

        HubShippingDeliver hubShippingDeliver = ConvertUtil.convert(form, HubShippingDeliver.class);
        hubShippingDeliver.setModelType(checkOrder.getModelType());
        hubShippingDeliver.setTransType("陆运");
        hubShippingDeliver.setDeliverType(2);
        hubShippingDeliver.setHubName("深圳仓");
        hubShippingDeliver.setCheckStateFlag("Y");
        hubShippingDeliver.setFLevel(1);
        hubShippingDeliver.setFStep(1);
        hubShippingDeliver.setStateFlag(2);
        HubShippingDeliver hubShippingDeliver1 = this.hubShippingDeliverService.saveHubShippingDeliver(hubShippingDeliver);
        if(hubShippingDeliver1 == null){
            log.warn("调度策略单创建失败");
        }

        checkOrder.setShippingDeliverId(hubShippingDeliver.getId());
        boolean result = this.checkOrderService.saveOrUpdate(checkOrder);
        if(result){
            log.warn("提验货单调度成功");
        }

    }

    @ApiOperation(value = "接受oms内陆运输返回的错误信息")
    @PostMapping(value = "/checkOrder/getErrorInformation")
    public void getErrorInformation(@RequestBody Map<String,Object> map) {

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


    }


}
