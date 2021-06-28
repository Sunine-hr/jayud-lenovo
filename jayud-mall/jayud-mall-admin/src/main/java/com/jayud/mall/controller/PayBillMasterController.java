package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.PayBillMasterVO;
import com.jayud.mall.service.IPayBillMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/paybillmaster")
@Api(tags = "A047-admin-应付账单接口")
@ApiSort(value = 47)
public class PayBillMasterController {

    @Autowired
    IPayBillMasterService payBillMasterService;

    //生成应付账单
    @ApiOperation(value = "生成应付账单")
    @PostMapping("/createPayBill")
    @ApiOperationSupport(order = 1)
    public CommonResult<PayBillMasterVO> createPayBill(@Valid @RequestBody PayBillForm form){
        return payBillMasterService.createPayBill(form);
    }


    //生成应付账单-确认
    //affirm
    @ApiOperation(value = "生成应付账单-确认")
    @PostMapping("/affirmPayBill")
    @ApiOperationSupport(order = 2)
    public CommonResult<PayBillMasterVO> affirmPayBill(@Valid @RequestBody PayBillMasterForm form){
        return payBillMasterService.affirmPayBill(form);
    }


    //生成应付账单-取消（前端取消）

    //应付账单分页查询
    @ApiOperation(value = "应付账单分页查询")
    @PostMapping("/findPayBillMasterByPage")
    @ApiOperationSupport(order = 3)
    public CommonResult<CommonPageResult<PayBillMasterVO>> findPayBillMasterByPage(
            @RequestBody QueryPayBillMasterForm form) {
        IPage<PayBillMasterVO> pageList = payBillMasterService.findPayBillMasterByPage(form);
        CommonPageResult<PayBillMasterVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //应付账单-查看明细
    //lookDetail
    @ApiOperation(value = "应付账单-查看明细")
    @PostMapping("/lookDetail")
    @ApiOperationSupport(order = 4)
    public CommonResult<PayBillMasterVO> lookDetail(@Valid @RequestBody BillMasterForm form){
        Long id = form.getId();
        return payBillMasterService.lookDetail(id);
    }

    //根据订单id，查询应付账单list
    @ApiOperation(value = "根据订单id，查询应付账单list")
    @PostMapping("/findPayBillMasterByOrderId")
    @ApiOperationSupport(order = 5)
    public CommonResult<List<PayBillMasterVO>> findPayBillMasterByOrderId(@RequestBody OrderInfoParaForm form) {
        Long orderId = form.getId();
        List<PayBillMasterVO> payBillMasterList = payBillMasterService.findPayBillMasterByOrderId(orderId);
        return CommonResult.success(payBillMasterList);
    }


}
