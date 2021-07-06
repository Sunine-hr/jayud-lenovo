package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.BillTaskRelevanceIdForm;
import com.jayud.mall.model.bo.BillTaskRelevanceQueryForm;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.service.IBillTaskRelevanceService;
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
@RequestMapping("/billtaskrelevance")
@Api(tags = "A060-admin-提单任务接口")
@ApiSort(value = 60)
public class BillTaskRelevanceController {

    @Autowired
    IBillTaskRelevanceService billTaskRelevanceService;

    @ApiOperation(value = "1.查询提单任务list")
    @PostMapping("/findBillTaskRelevance")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<BillTaskRelevanceVO>> findBillTaskRelevance(@Valid @RequestBody BillTaskRelevanceQueryForm form) {
        List<BillTaskRelevanceVO> list = billTaskRelevanceService.findBillTaskRelevance(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "2.根据id查询提单任务")
    @PostMapping("/findBillTaskRelevanceById")
    @ApiOperationSupport(order = 2)
    public CommonResult<BillTaskRelevanceVO> findBillTaskRelevanceById(@Valid @RequestBody BillTaskRelevanceIdForm form){
        Long id = form.getId();
        BillTaskRelevanceVO billTaskRelevanceVO = billTaskRelevanceService.findBillTaskRelevanceById(id);
        return CommonResult.success(billTaskRelevanceVO);
    }

    @ApiOperation(value = "3.完成提单任务")
    @PostMapping("/finishTask")
    @ApiOperationSupport(order = 3)
    public CommonResult finishTask(@Valid @RequestBody BillTaskRelevanceIdForm form){
        billTaskRelevanceService.finishTask(form);
        return CommonResult.success("操作成功");
    }

    @ApiOperation(value = "4.延期提单任务")
    @PostMapping("/postponeTask")
    @ApiOperationSupport(order = 4)
    public CommonResult postponeTask(@Valid @RequestBody BillTaskRelevanceIdForm form){
        billTaskRelevanceService.postponeTask(form);
        return CommonResult.success();
    }


}
