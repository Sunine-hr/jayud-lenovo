package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryCurrencyRateForm;
import com.jayud.mall.model.vo.CurrencyRateVO;
import com.jayud.mall.service.ICurrencyRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/currencyrate")
@Api(tags = "币种汇率接口")
public class CurrencyRateController {

    @Autowired
    ICurrencyRateService currencyRateService;

    @ApiOperation(value = "查询币种汇率List")
    @PostMapping("/findCurrencyRate")
    public CommonResult<List<CurrencyRateVO>> findCurrencyRate(@RequestBody QueryCurrencyRateForm form) {
        List<CurrencyRateVO> list = currencyRateService.findCurrencyRate(form);
        return CommonResult.success(list);
    }


}
