package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanWaybillCaseRelationForm;
import com.jayud.mall.service.IOceanWaybillCaseRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oceanwaybillcaserelation")
@Api(tags = "运单对应箱号接口-准备删除的接口")
public class OceanWaybillCaseRelationController {

    @Autowired
    IOceanWaybillCaseRelationService oceanWaybillCaseRelationService;

    @ApiOperation(value = "保存-运单对应箱号接口")
    @PostMapping("/saveOceanWaybillCaseRelation")
    public CommonResult saveOceanWaybillCaseRelation(@RequestBody OceanWaybillCaseRelationForm form){
        return oceanWaybillCaseRelationService.saveOceanWaybillCaseRelation(form);
    }

}
