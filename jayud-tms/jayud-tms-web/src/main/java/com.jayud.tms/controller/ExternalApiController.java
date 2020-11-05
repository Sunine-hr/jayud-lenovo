package com.jayud.tms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.bo.TmsChangeStatusForm;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.InitChangeStatusVO;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.service.IOrderTransportService;
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
@Api(tags = "tms对外接口")
public class ExternalApiController {

    @Autowired
    IOrderTransportService orderTransportService;

    @Autowired
    RedisUtils redisUtils;



    @ApiOperation(value = "创建中港子订单")
    @RequestMapping(value = "/api/createOrderTransport")
    ApiResult createOrderTransport(@RequestBody InputOrderTransportForm form){
        Boolean result = orderTransportService.createOrderTransport(form);
        return ApiResult.ok(result);
    }


    @ApiOperation(value = "获取中港子订单详情")
    @RequestMapping(value = "/api/getOrderTransport")
    ApiResult getOrderTransport(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InputOrderTransportVO inputOrderTransportVO = orderTransportService.getOrderTransport(mainOrderNo);
        return ApiResult.ok(inputOrderTransportVO);
    }

    @ApiOperation(value = "获取中港订单号")
    @RequestMapping(value = "/api/getTransportOrderNo")
    ApiResult getTransportOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO,mainOrderNo);
        OrderTransport orderTransport = orderTransportService.getOne(queryWrapper);
        if(orderTransport != null){
            initChangeStatusVO.setId(orderTransport.getId());
            initChangeStatusVO.setOrderNo(orderTransport.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.ZGYS);
            initChangeStatusVO.setStatus(orderTransport.getStatus());
            initChangeStatusVO.setNeedInputCost(orderTransport.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    @ApiOperation(value = "更改中港单状态")
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeTransportStatus(@RequestBody List<TmsChangeStatusForm> form){
        for (TmsChangeStatusForm tms : form) {
            OrderTransport orderTransport = new OrderTransport();
            orderTransport.setStatus(tms.getStatus());
            orderTransport.setNeedInputCost(tms.getNeedInputCost());
            orderTransport.setUpdatedUser(tms.getLoginUser());
            orderTransport.setUpdatedTime(LocalDateTime.now());
            QueryWrapper<OrderTransport> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq(SqlConstant.ORDER_NO,tms.getOrderNo());
            orderTransportService.update(orderTransport,updateWrapper);
        }
        return ApiResult.ok();
    }


}









    



