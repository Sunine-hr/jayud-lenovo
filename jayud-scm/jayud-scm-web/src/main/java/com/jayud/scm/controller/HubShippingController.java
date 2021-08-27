package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddHubShippingForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.model.vo.HubReceivingVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.IHubShippingService;
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
        boolean result = hubShippingService.signOrder(form);
        if(!result){
            return CommonResult.error(444,"订单签收失败");
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

}

