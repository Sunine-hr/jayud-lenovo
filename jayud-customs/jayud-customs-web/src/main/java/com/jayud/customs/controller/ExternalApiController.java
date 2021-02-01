package com.jayud.customs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.customs.model.bo.CustomsChangeStatusForm;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.InitChangeStatusVO;
import com.jayud.customs.model.vo.InputOrderCustomsVO;
import com.jayud.customs.model.vo.OrderCustomsVO;
import com.jayud.customs.service.IOrderCustomsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "customs对外接口")
public class ExternalApiController {

    @Autowired
    IOrderCustomsService orderCustomsService;

    @Autowired
    RedisUtils redisUtils;


    @ApiOperation(value = "获取子订单信息")
    @RequestMapping(value = "/api/getCustomsOrderNum")
    ApiResult getCustomsOrderNum(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        int customsNum = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("main_order_no", mainOrderNo);
        List<OrderCustomsVO> orderCustomsVOS = orderCustomsService.findOrderCustomsByCondition(param);
        if (orderCustomsVOS != null) {
            customsNum = orderCustomsVOS.size();
        }
        return ApiResult.ok(customsNum);
    }

    @ApiOperation(value = "获取子订单详情")
    @RequestMapping(value = "/api/getCustomsDetail")
    ApiResult getCustomsDetail(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        InputOrderCustomsVO inputOrderCustomsVO = orderCustomsService.getOrderCustomsDetail(mainOrderNo);
        return ApiResult.ok(inputOrderCustomsVO);
    }


    /**
     * 创建报关单
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/api/createOrderCustoms")
    ApiResult createOrderCustoms(@RequestBody InputOrderCustomsForm form) {
        boolean result = orderCustomsService.oprOrderCustoms(form);
        return ApiResult.ok(result);
    }

    @ApiOperation(value = "获取报关订单单号")
    @RequestMapping(value = "/api/findCustomsOrderNo")
    ApiResult findCustomsOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.MAIN_ORDER_NO, mainOrderNo);
        List<OrderCustoms> orderCustoms = orderCustomsService.list(queryWrapper);
        List<InitChangeStatusVO> changeStatusVOS = new ArrayList<>();
        for (OrderCustoms orderCustom : orderCustoms) {
            InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
            initChangeStatusVO.setId(orderCustom.getId());
            initChangeStatusVO.setOrderNo(orderCustom.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.BG);
            initChangeStatusVO.setNeedInputCost(orderCustom.getNeedInputCost());
            initChangeStatusVO.setStatus(orderCustom.getStatus());
            changeStatusVOS.add(initChangeStatusVO);
        }
        return ApiResult.ok(changeStatusVOS);
    }

    @ApiOperation(value = "修改报关状态")
    @RequestMapping(value = "/api/changeCustomsStatus")
    ApiResult changeCustomsStatus(@RequestBody List<CustomsChangeStatusForm> form) {
        for (CustomsChangeStatusForm customs : form) {
            OrderCustoms orderCustoms = new OrderCustoms();
            orderCustoms.setStatus(customs.getStatus());
            orderCustoms.setNeedInputCost(customs.getNeedInputCost());
            orderCustoms.setUpdatedUser(customs.getLoginUser());
            orderCustoms.setUpdatedTime(LocalDateTime.now());
            QueryWrapper<OrderCustoms> updateWrapper = new QueryWrapper<>();
            updateWrapper.eq(SqlConstant.ORDER_NO, customs.getOrderNo());
            orderCustomsService.update(orderCustoms, updateWrapper);
        }
        return ApiResult.ok();
    }


    @ApiOperation(value = "根据主订单集合查询所有报关信息")
    @RequestMapping(value = "/api/getCustomsOrderByMainOrderNos")
    ApiResult getCustomsOrderByMainOrderNos(@RequestParam("mainOrderNos") List<String> mainOrderNos) {
        return ApiResult.ok(this.orderCustomsService.getCustomsOrderByMainOrderNos(mainOrderNos));
    }

}









    



