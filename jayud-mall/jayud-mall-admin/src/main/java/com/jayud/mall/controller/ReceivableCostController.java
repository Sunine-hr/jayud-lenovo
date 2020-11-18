package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ReceivableCostForm;
import com.jayud.mall.model.po.ReceivableCost;
import com.jayud.mall.service.IReceivableCostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receivablecost")
@Api(tags = "应收/应付费用名称接口")
public class ReceivableCostController {

    @Autowired
    IReceivableCostService receivableCostService;

    @ApiOperation(value = "查询应收/应付费用名称List")
    @PostMapping("/findReceivableCost")
    public CommonResult<List<ReceivableCost>> findReceivableCost(@RequestBody ReceivableCostForm form) {
        List<ReceivableCost> list = receivableCostService.findReceivableCost(form);
        return CommonResult.success(list);
    }


}
