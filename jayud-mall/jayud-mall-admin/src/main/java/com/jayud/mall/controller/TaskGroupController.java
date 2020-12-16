package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.vo.TaskGroupVO;
import com.jayud.mall.service.ITaskGroupService;
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
@RequestMapping("/taskgroup")
@Api(tags = "后台-任务分组接口")
@ApiSort(value = 10001)
public class TaskGroupController {

    @Autowired
    ITaskGroupService taskGroupService;

    @ApiOperation(value = "查询任务分组List")
    @PostMapping("/findTaskGroup")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TaskGroupVO>> findTaskGroup(@RequestBody TaskGroupForm form) {
        List<TaskGroupVO> list = taskGroupService.findTaskGroup(form);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "查询提单任务分组List")
    @PostMapping("/findTaskGroupByTd")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<TaskGroupVO>> findTaskGroupByTd(){
        List<TaskGroupVO> list = taskGroupService.findTaskGroupByTd();
        return CommonResult.success(list);
    }

    @ApiOperation(value = "查询运单任务分组List")
    @PostMapping("/findTaskGroupByYd")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<TaskGroupVO>> findTaskGroupByYd(){
        List<TaskGroupVO> list = taskGroupService.findTaskGroupByYd();
        return CommonResult.success(list);
    }


}
