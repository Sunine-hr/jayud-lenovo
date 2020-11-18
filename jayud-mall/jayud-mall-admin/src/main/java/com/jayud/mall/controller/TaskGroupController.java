package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.po.TaskGroup;
import com.jayud.mall.service.ITaskGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taskgroup")
@Api(tags = "提单任务分组接口")
public class TaskGroupController {

    @Autowired
    ITaskGroupService taskGroupService;

    @ApiOperation(value = "查询提单任务分组List")
    @PostMapping("/findTaskGroup")
    public CommonResult<List<TaskGroup>> findTaskGroup(@RequestBody TaskGroupForm form) {
        List<TaskGroup> list = taskGroupService.findTaskGroup(form);
        return CommonResult.success(list);
    }


}
