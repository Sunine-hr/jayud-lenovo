package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.*;
import com.jayud.scm.model.enums.CheckStateEnum;
import com.jayud.scm.model.po.BookingOrder;
import com.jayud.scm.model.po.CheckOrder;
import com.jayud.scm.model.vo.CheckOrderVO;
import com.jayud.scm.service.IBookingOrderService;
import com.jayud.scm.service.ICheckOrderService;
import com.jayud.scm.service.IHgBillService;
import com.jayud.scm.service.IHubReceivingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提验货主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/checkOrder")
@Api(tags = "提验货管理")
@Slf4j
public class CheckOrderController {

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private IHubReceivingService hubReceivingService;

    @Autowired
    private IBookingOrderService bookingOrderService;

    @Autowired
    private IHgBillService hgBillService;

    @ApiOperation(value = "下一步操作 1提货完成、2验货完成、3提货撤销、4验货撤销")
    @PostMapping(value = "/nextOperation")
    public CommonResult nextOperation(@RequestBody QueryCommonForm form) {
        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        String checkState = checkOrder.getCheckState();
//        if(checkState.equals("0")){
//            return CommonResult.error(444,"订单未审核，请前往审核");
//        }
        if(checkState.equals("2")){
            return CommonResult.error(444,"订单已取消，无法操作");
        }
        switch (form.getNext()){
            case 1:
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_3.getCode().toString());
                break;
            case 2:
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_5.getCode().toString());
                break;
            case 3:
                if(!checkState.equals("3")){
                    return CommonResult.error(444,"只有当前状态为提货完成的单，才能进行提货撤销");
                }
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_1.getCode().toString());
                break;
            case 4:
                if(!checkState.equals("4")){
                    return CommonResult.error(444,"只有当前状态为验货完成的单，才能进行验货撤销");
                }
                checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_3.getCode().toString());
                break;
        }
        boolean update = checkOrderService.updateById(checkOrder);
        if(!update){
            return CommonResult.error(444,"操作失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "入库") // TODO: 2021/8/23  入库操作未完成
    @PostMapping(value = "/warehousing")
    @Transactional
    public CommonResult warehousing(@RequestBody QueryCommonForm form) {
        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_6.getCode().toString());
        boolean result = hubReceivingService.addHubReceiving(form);
        if(!result){
            return CommonResult.error(444,"入库失败");
        }
        boolean update = checkOrderService.updateById(checkOrder);
        if(!update){
            return CommonResult.error(444,"操作失败");
        }
        //判断是否入库完成，入库完成增加一个报关单
        boolean result1 = bookingOrderService.isCommplete(checkOrder.getBookingId());
        if(result1){
            //新增一个报关单
            boolean save = hgBillService.addHgBill(checkOrder.getBookingId());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据委托单id获取提验货信息")
    @PostMapping(value = "/getCheckOrderByBookingId")
    public CommonResult<List<CheckOrderVO>> getCheckOrderByBookingId(@RequestBody QueryCommonForm form) {
        List<BookingOrder> bookingOrderByHgTrackId = bookingOrderService.getBookingOrderByHgTrackId(form.getId());
        List<Integer> list = new ArrayList<>();
        for (BookingOrder bookingOrder : bookingOrderByHgTrackId) {
            list.add(bookingOrder.getId());
        }
        form.setIds(list);
        List<CheckOrderVO> checkOrderVOList = checkOrderService.getCheckOrderByBookingId(form);
        return CommonResult.success(checkOrderVOList);
    }

    @ApiOperation(value = "自动生成提验货单")
    @PostMapping(value = "/automaticGenerationCheckOrder")
    public CommonResult automaticGenerationCheckOrder(@RequestBody QueryCommonForm form) {
        return checkOrderService.automaticGenerationCheckOrder(form);

    }

    @ApiOperation(value = "新增提验货单")
    @PostMapping(value = "/saveOrUpdateCheckOrder")
    public CommonResult saveOrUpdateCheckOrder(@RequestBody AddCheckOrderForm form) {
//        List<AddCheckOrderEntryForm> checkOrderEntryForms = form.getCheckOrderEntryForms();
//        for (AddCheckOrderEntryForm checkOrderEntryForm : checkOrderEntryForms) {
//
//        }
        boolean result = checkOrderService.saveOrUpdateCheckOrder(form);
        if(!result){
            return CommonResult.error(444,"新增或修改提货验货单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id获取提验货单信息")
    @PostMapping(value = "/getCheckOrderById")
    public CommonResult<CheckOrderVO> getCheckOrderById(@RequestBody QueryForm form) {
        return CommonResult.success(this.checkOrderService.getCheckOrderById(form.getId()));
    }

}

