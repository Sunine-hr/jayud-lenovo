package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryCurrencyInfoForm;
import com.jayud.mall.model.vo.CurrencyInfoVO;
import com.jayud.mall.service.ICurrencyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencyinfo")
@Api(tags = "S009-后台-币种接口")
@ApiSort(value = 9)
public class CurrencyInfoController {

    @Autowired
    ICurrencyInfoService currencyInfoService;

    @ApiOperation(value = "查询币种List")
    @PostMapping("/findCurrencyInfo")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<CurrencyInfoVO>> findCurrencyInfo(@RequestBody QueryCurrencyInfoForm form) {
        List<CurrencyInfoVO> list = currencyInfoService.findCurrencyInfo(form);
        return CommonResult.success(list);
    }


}
