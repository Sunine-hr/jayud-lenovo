package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OrderBillForm;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.service.IReceivableBillMasterService;
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
@RequestMapping("/receivablebillmaster")
@Api(tags = "A046-admin-应收账单接口")
@ApiSort(value = 46)
public class ReceivableBillMasterController {

    @Autowired
    IReceivableBillMasterService receivableBillMasterService;


    //生成应收账单
    @ApiOperation(value = "生成应收账单")
    @PostMapping("/createReceivableBill")
    @ApiOperationSupport(order = 1)
    public CommonResult<ReceivableBillMasterVO> createReceivableBill(@Valid @RequestBody OrderBillForm form){



//        return receivableBillMasterService.createReceivableBill();
        return null;
    }


}
