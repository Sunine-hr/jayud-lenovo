package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CountryForm;
import com.jayud.mall.model.vo.CountryVO;
import com.jayud.mall.service.ICountryService;
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
@RequestMapping("/country")
@Api(tags = "S008-后台-国家基础信息接口")
@ApiSort(value = 8)
public class CountryController {

    @Autowired
    ICountryService countryService;

    @ApiOperation(value = "查询国家list")
    @PostMapping("/findCountry")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<CountryVO>> findCountry(@RequestBody CountryForm form) {
        List<CountryVO> list = countryService.findCountry(form);
        return CommonResult.success(list);
    }
}
