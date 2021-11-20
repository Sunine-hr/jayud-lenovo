package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHubShippingForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.HubShipping;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.IHubShippingService;
import com.jayud.scm.service.impl.CommodityServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/hubShipping")
@Api(tags = "出库管理")
public class HubShippingController {

    @Autowired
    private IHubShippingService hubShippingService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @ApiOperation(value = "根据id查询出库订单信息")
    @PostMapping(value = "/getHubShippingById")
    public CommonResult<HubShippingVO> getHubShippingById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");

        HubShippingVO hubShippingVO = hubShippingService.getHubShippingById(id);
        hubShippingVO.setModelTypeName(hubShippingVO.getModelType().toString());

        return CommonResult.success(hubShippingVO);
    }

    @ApiOperation(value = "新增或修改出库订单")
    @PostMapping(value = "/saveOrUpdateHubShipping")
    public CommonResult saveOrUpdateHubShipping(@RequestBody AddHubShippingForm form) {
        form.setModelType(Integer.parseInt(form.getModelTypeName()));
        boolean result = hubShippingService.saveOrUpdateHubShipping(form);
        if(!result){
            return CommonResult.error(444,"新增或修改出库订单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "签收出库单")
    @PostMapping(value = "/signOrder")
    public CommonResult signOrder(@RequestBody QueryCommonForm form) {

        HubShipping hubShipping = hubShippingService.getById(form.getIds().get(0));
        if(hubShipping.getSignState().equals(1)){
            return CommonResult.error(444,"该出库单已签收，无法重复签收");
        }

        if(hubShipping.getStateFlag().equals(1)){
            return CommonResult.error(444,"未出库的出库单不允许签收！");
        }

        boolean result = hubShippingService.signOrder(form);
        if(!result){
            return CommonResult.error(444,"出库单签收失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据委托单id获取出库单信息")
    @PostMapping(value = "/getHubShippingByBookingId")
    public CommonResult getHubShippingByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            list.add(bookingOrder.getId());
        }
        form.setIds(list);
        List<HubShippingVO> hubShippingVOS = hubShippingService.getHubShippingByBookingId(form);
        return CommonResult.success(hubShippingVOS);
    }

    @ApiOperation(value = "自动出库")
    @PostMapping(value = "/automaticGenerationHubShipping")
    public CommonResult automaticGenerationHubShipping(@RequestBody QueryCommonForm form) {
        return hubShippingService.automaticGenerationHubShipping(form);
    }


    @ApiOperation(value = "出库单信息修改")
    @PostMapping(value = "/updateHubShipping")
    public CommonResult updateHubShipping(@RequestBody AddHubShippingForm form) {
        boolean result = hubShippingService.updateHubShipping(form);
        if(!result){
            return CommonResult.error(444,"出库单信息修改失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "签收出库单信息")
    @PostMapping(value = "/signHubShipping")
    public CommonResult signHubShipping(@RequestBody QueryCommonForm form) {
        if(null == form.getId()){
            return CommonResult.error(444,"出库单id不为空");
        }
        HubShipping hubShipping = hubShippingService.getById(form.getId());
        if(hubShipping.getStateFlag().equals(1)){
            return CommonResult.error(444,"未出库的出库单不允许签收！");
        }
        boolean result = hubShippingService.signHubShipping(form);
        if(!result){
            return CommonResult.error(444,"签收出库单信息失败");
        }
        return CommonResult.success();
    }

}

