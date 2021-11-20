package com.jayud.scm.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.RSAUtils;
import com.jayud.scm.model.bo.AddHubShippingDeliverForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShipping;
import com.jayud.scm.model.po.HubShippingDeliver;
import com.jayud.scm.service.IHubShippingDeliverService;
import com.jayud.scm.service.IHubShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //  进口提货走港车，送货走内陆车   出库单订车
    @ApiOperation(value = "出库单推送信息至oms内陆运输")
    @PostMapping(value = "/pushHubShipping")
    public CommonResult pushHubShipping(@RequestBody QueryCommonForm form) {
        return CommonResult.success();
    }



    @ApiOperation(value = "接受oms内陆运输返回的信息")
    @PostMapping(value = "/hubShipping/getDataByOms")
    public void getDataByOms(@RequestBody Map<String,Object> map) {

        String data = MapUtil.getStr(map, "data");

        String s = null;
        try {
            s = RSAUtils.publicDecrypt(data, RSAUtils.getPublicKey(RSAUtils.PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.warn("出库参数接收成功："+s);

        AddHubShippingDeliverForm form = JSONUtil.toBean(s, AddHubShippingDeliverForm.class);

        QueryWrapper<HubShipping> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HubShipping::getShippingNo,form.getShippingNo());
        queryWrapper.lambda().eq(HubShipping::getVoided,0);
        HubShipping hubShipping = this.hubShippingService.getOne(queryWrapper);

        HubShippingDeliver shippingDeliver = ConvertUtil.convert(form, HubShippingDeliver.class);
        shippingDeliver.setModelType(hubShipping.getModelType());
        shippingDeliver.setTransType("陆运");
        shippingDeliver.setDeliverType(2);
        shippingDeliver.setHubName("深圳仓");
        shippingDeliver.setCheckStateFlag("Y");
        shippingDeliver.setFLevel(1);
        shippingDeliver.setFStep(1);
        shippingDeliver.setStateFlag(2);
        HubShippingDeliver hubShippingDeliver1 = this.hubShippingDeliverService.saveHubShippingDeliver(shippingDeliver);
        if(hubShippingDeliver1 == null){
            log.warn("调度策略单创建失败");
        }

        hubShipping.setStateFlag(2);
        hubShipping.setDeliverId(hubShippingDeliver1.getId());
        hubShipping.setDeliverNo(hubShippingDeliver1.getDeliverNo());
        boolean result = this.hubShippingService.saveOrUpdate(hubShipping);
        if(result){
            log.warn("出库单调度成功");
        }
    }

    @ApiOperation(value = "接受oms内陆运输返回的错误信息")
    @PostMapping(value = "/hubShipping/getErrorInformation")
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

        QueryWrapper<HubShipping> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(HubShipping::getShippingNo,map1.get("shippingNo"));
        queryWrapper.lambda().eq(HubShipping::getVoided,0);
        HubShipping hubShipping = this.hubShippingService.getOne(queryWrapper);



    }


}
