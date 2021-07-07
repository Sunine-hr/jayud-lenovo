package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.WaybillTaskRelevanceIdForm;
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

import javax.validation.Valid;
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
    public CommonResult<List<WaybillTaskRelevanceVO>> findWaybillTaskRelevance(@Valid @RequestBody WaybillTaskRelevanceQueryForm form) {
        List<WaybillTaskRelevanceVO> list = waybillTaskRelevanceService.findWaybillTaskRelevance(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "2.根据id查询运单任务")
    @PostMapping("/findWaybillTaskRelevanceById")
    @ApiOperationSupport(order = 2)
    public CommonResult<WaybillTaskRelevanceVO> findWaybillTaskRelevanceById(@Valid @RequestBody WaybillTaskRelevanceIdForm form){
        Long id = form.getId();
        WaybillTaskRelevanceVO waybillTaskRelevanceVO = waybillTaskRelevanceService.findWaybillTaskRelevanceById(id);
        return CommonResult.success(waybillTaskRelevanceVO);
    }

    @ApiOperation(value = "3.完成运单任务")
    @PostMapping("/finishTask")
    @ApiOperationSupport(order = 3)
    public CommonResult finishTask(@Valid @RequestBody WaybillTaskRelevanceIdForm form){
        waybillTaskRelevanceService.finishTask(form);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "4.延期运单任务")
    @PostMapping("/postponeTask")
    @ApiOperationSupport(order = 4)
    public CommonResult postponeTask(@Valid @RequestBody WaybillTaskRelevanceIdForm form){
        waybillTaskRelevanceService.postponeTask(form);
        return CommonResult.success();
    }


}
