package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PayBillForm;
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


}
