package com.jayud.scm.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.HgBill;
import com.jayud.scm.model.vo.HgBillVO;
import com.jayud.scm.model.vo.HubShippingVO;
import com.jayud.scm.model.vo.SingleWindowData;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.IHgBillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关单主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/hgBill")
@Api(tags = "报关列表管理")
public class HgBillController {

    @Autowired
    private IHgBillService hgBillService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @ApiOperation(value = "根据委托单id获取报关单信息")
    @PostMapping(value = "/getHgBillByBookingId")
    public CommonResult getHgBillByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            if(bookingOrder.getBillId() != null){
                list.add(bookingOrder.getBillId());
            }
        }
        form.setIds(list);
        List<HgBillVO> hgBillVOS = hgBillService.getHgBillByBookingId(form);
        return CommonResult.success(hgBillVOS);
    }

    @ApiOperation(value = "报关单号日期录入")
    @PostMapping(value = "/entryCustomDate")
    public CommonResult entryCustomDate(@RequestBody QueryCommonForm form) {
        boolean update = hgBillService.entryCustomDate(form);
        if(!update){
            return CommonResult.error(444,"报关单号日期录入操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取所有状态为已提交的报关单")
    @PostMapping(value = "/getHgBillDataByDeclareState")
    public CommonResult getHgBillDataByDeclareState(QueryCommonForm form) {
        form.setDeclareState(1);
        List<HgBillVO> hgBillVOS = hgBillService.getHgBillDataByDeclareState(form);
        return CommonResult.success(hgBillVOS);
    }

    @ApiOperation(value = "获取推送单一窗口的数据")
    @PostMapping(value = "/getSingleWindowData")
    public CommonResult getSingleWindowData(@RequestBody QueryCommonForm form) {
        List<SingleWindowData> singleWindowData = hgBillService.getSingleWindowData(form);
        if(CollectionUtil.isEmpty(singleWindowData)){
            return CommonResult.error(444,"不存在该单号数据，或者该报关单未审核");
        }
        return CommonResult.success(singleWindowData);
    }


    @ApiOperation(value = "提交单一窗口")
    @PostMapping(value = "/submitSingleWindow")
    public CommonResult submitSingleWindow(@RequestBody QueryCommonForm form) {
        boolean update = hgBillService.submitSingleWindow(form);
        if(!update){
            return CommonResult.error(444,"提交单一窗口操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "修改状态或者报关数据")
    @PostMapping(value = "/updateHgBill")
    public CommonResult updateHgBill(@RequestBody QueryCommonForm form) {

        if(form.getBillNo() != null){
            QueryWrapper<HgBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(HgBill::getBillNo,form.getBillNo());
            queryWrapper.lambda().eq(HgBill::getVoided,0);
            HgBill hgBill = hgBillService.getOne(queryWrapper);
            if(hgBill == null){
                return CommonResult.error(444,"该报关单不存在或该报关单已被删除");
            }
        }

        boolean update = hgBillService.updateHgBill(form);
        if(!update){
            return CommonResult.error(444,"修改状态或者报关数据,操作失败");
        }
        return CommonResult.success();
    }



}

