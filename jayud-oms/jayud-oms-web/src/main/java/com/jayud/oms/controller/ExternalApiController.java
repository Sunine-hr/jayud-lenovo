package com.jayud.oms.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.InputOrderForm;
import com.jayud.oms.model.bo.OprStatusForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.vo.InputOrderVO;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@Api(tags = "oms对外接口")
public class ExternalApiController {

    @Autowired
    IOrderInfoService orderInfoService;

    @Autowired
    ILogisticsTrackService logisticsTrackService;

    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "保存主订单")
    @RequestMapping(value = "/api/oprMainOrder")
    public ApiResult oprMainOrder(@RequestBody InputOrderForm form) {
        String result = orderInfoService.oprMainOrder(form);
        if(result != null){
            return ApiResult.ok(result);
        }
        return ApiResult.error();
    }


    @ApiOperation(value = "获取主订单信息")
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult getMainOrderById(@RequestParam(value = "idValue") Long idValue){
        InputOrderVO inputOrderVO = orderInfoService.getMainOrderById(idValue);
        return ApiResult.ok(inputOrderVO);
    }


    @ApiOperation(value = "保存操作状态")
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form){
        LogisticsTrack logisticsTrack = ConvertUtil.convert(form,LogisticsTrack.class);
        logisticsTrack.setCreatedUser(getLoginUser());
        logisticsTrack.setCreatedTime(LocalDateTime.now());
        logisticsTrackService.saveOrUpdate(logisticsTrack);
        return ApiResult.ok();
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return loginUser;
    }


}









    



