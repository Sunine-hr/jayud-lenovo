package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.service.IOceanCounterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oceancounter")
@Api(tags = "S041-后台-提单对应货柜信息接口")
@ApiSort(value = 41)
public class OceanCounterController {

    @Autowired
    IOceanCounterService oceanCounterService;

    @ApiOperation(value = "保存-提单对应货柜信息,没有用了")
    @PostMapping("/saveOceanCounter")
    @ApiOperationSupport(order = 1)
    public CommonResult saveOceanCounter(@RequestBody OceanCounterForm form){
        return oceanCounterService.saveOceanCounter(form);
    }

}
