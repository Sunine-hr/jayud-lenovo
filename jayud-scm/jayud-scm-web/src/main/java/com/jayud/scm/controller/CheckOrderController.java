package com.jayud.scm.controller;


import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.PermissionForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.CheckStateEnum;
import com.jayud.scm.model.po.CheckOrder;
import com.jayud.scm.service.ICheckOrderService;
import com.jayud.scm.service.IHubReceivingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
public class CheckOrderController {

    @Autowired
    private ICheckOrderService checkOrderService;

    @Autowired
    private IHubReceivingService hubReceivingService;

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
    public CommonResult warehousing(@RequestBody QueryCommonForm form) {
        CheckOrder checkOrder = checkOrderService.getById(form.getId());
        checkOrder.setCheckState(CheckStateEnum.CHECK_STATE_6.getCode().toString());
        boolean result = hubReceivingService.addHubReceiving(form.getId());
        if(!result){
            return CommonResult.error(444,"入库失败");
        }
        boolean update = checkOrderService.updateById(checkOrder);
        if(!update){
            return CommonResult.error(444,"操作失败");
        }
        return CommonResult.success();
    }

}

