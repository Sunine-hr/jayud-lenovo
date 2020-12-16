package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OceanCounterWaybillRelationForm;
import com.jayud.mall.model.vo.OceanCounterWaybillRelationVO;
import com.jayud.mall.service.IOceanCounterWaybillRelationService;
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
@RequestMapping("/oceancounteryaybillrelation")
@Api(tags = "后台-货柜对应运单(订单)关联接口")
@ApiSort(value = 10005)
public class OceanCounterWaybillRelationController {

    @Autowired
    IOceanCounterWaybillRelationService oceanCounterWaybillRelationService;

    @ApiOperation(value = "保存-货柜对应运单(订单)关联表")
    @PostMapping("/saveOceanCounterWaybillRelation")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OceanCounterWaybillRelationVO>> saveOceanCounterWaybillRelation(
            @RequestBody List<OceanCounterWaybillRelationForm> forms){
        List<OceanCounterWaybillRelationVO> list =
                oceanCounterWaybillRelationService.saveOceanCounterWaybillRelation(forms);
        return CommonResult.success(list);
    }

}
