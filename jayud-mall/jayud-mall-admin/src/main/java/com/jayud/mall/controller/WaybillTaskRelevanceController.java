package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.WaybillTaskRelevanceQueryForm;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.service.IWaybillTaskRelevanceService;
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
@RequestMapping("/waybilltaskrelevance")
@Api(tags = "A059-admin-运单任务接口")
@ApiSort(value = 59)
public class WaybillTaskRelevanceController {

    @Autowired
    IWaybillTaskRelevanceService waybillTaskRelevanceService;

    @ApiOperation(value = "1.查询运单任务list")
    @PostMapping("/findWaybillTaskRelevance")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<WaybillTaskRelevanceVO>> findWaybillTaskRelevance(@RequestBody WaybillTaskRelevanceQueryForm form) {
        List<WaybillTaskRelevanceVO> list = waybillTaskRelevanceService.findWaybillTaskRelevance(form);
        return CommonResult.success(list);
    }

//    public CommonResult<>


}
