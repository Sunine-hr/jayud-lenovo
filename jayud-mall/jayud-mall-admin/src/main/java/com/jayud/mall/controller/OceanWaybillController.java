package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanWaybillForm;
import com.jayud.mall.service.IOceanWaybillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oceanwaybill")
@Api(tags = "货柜对应运单接口-准备删除的接口")
public class OceanWaybillController {

    @Autowired
    IOceanWaybillService oceanWaybillService;

    @ApiOperation(value = "保存-货柜对应运单信息")
    @PostMapping("/saveOceanWaybill")
    public CommonResult saveOceanWaybill(@RequestBody OceanWaybillForm form){
        return oceanWaybillService.saveOceanWaybill(form);
    }

}
