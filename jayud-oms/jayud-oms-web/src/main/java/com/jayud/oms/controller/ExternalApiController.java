package com.jayud.oms.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.bo.HandleSubProcessForm;
import com.jayud.oms.model.bo.InputMainOrderForm;
import com.jayud.oms.model.bo.OprStatusForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.vo.InputMainOrderVO;
import com.jayud.oms.model.vo.OrderStatusVO;
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
import java.util.List;


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
    public ApiResult oprMainOrder(@RequestBody InputMainOrderForm form) {
        String result = orderInfoService.oprMainOrder(form);
        if(result != null){
            return ApiResult.ok(result);
        }
        return ApiResult.error();
    }


    @ApiOperation(value = "获取主订单信息")
    @RequestMapping(value = "/api/getMainOrderById")
    ApiResult getMainOrderById(@RequestParam(value = "id") Long id){
        InputMainOrderVO inputOrderVO = orderInfoService.getMainOrderById(id);
        return ApiResult.ok(inputOrderVO);
    }

    @ApiOperation(value = "获取主订单ID")
    @RequestMapping(value = "/api/getIdByOrderNo")
    ApiResult getIdByOrderNo(@RequestParam(value = "orderNo") String orderNo){
        Long mainOrderId = orderInfoService.getIdByOrderNo(orderNo);
        return ApiResult.ok(mainOrderId);
    }


    @ApiOperation(value = "保存操作状态")
    @RequestMapping(value = "/api/saveOprStatus")
    ApiResult saveOprStatus(@RequestBody OprStatusForm form){
        LogisticsTrack logisticsTrack = new LogisticsTrack();
        logisticsTrack.setMainOrderId(form.getMainOrderId());
        logisticsTrack.setOrderId(form.getOrderId());
        logisticsTrack.setStatus(form.getStatus());
        logisticsTrack.setStatusName(form.getStatusName());
        logisticsTrack.setOperatorUser(form.getOperatorUser());
        logisticsTrack.setOperatorTime(DateUtils.str2LocalDateTime(form.getOperatorTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setStatusPic(form.getStatusPic());
        logisticsTrack.setDescription(form.getDescription());
        logisticsTrack.setEntrustNo(form.getEntrustNo());
        logisticsTrack.setGoCustomsTime(DateUtils.str2LocalDateTime(form.getGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setPreGoCustomsTime(DateUtils.str2LocalDateTime(form.getPreGoCustomsTime(),DateUtils.DATE_TIME_PATTERN));
        logisticsTrack.setCreatedUser(String.valueOf(getLoginUser().getData()));
        logisticsTrack.setCreatedTime(LocalDateTime.now());
        logisticsTrackService.saveOrUpdate(logisticsTrack);
        return ApiResult.ok();
    }

    /**
     * 获取当前登录用户
     * @return
     */
    @RequestMapping(value = "/api/getLoginUser")
    ApiResult getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return ApiResult.ok(loginUser);
    }

    @RequestMapping(value = "/api/handleSubProcess")
    ApiResult handleSubProcess(@RequestBody HandleSubProcessForm form){
        List<OrderStatusVO> orderStatusVOS = orderInfoService.handleSubProcess(form);
        return ApiResult.ok(orderStatusVOS);
    }


}









    



