package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTaskForm;
import com.jayud.mall.model.bo.TaskForm;
import com.jayud.mall.model.bo.TaskParaForm;
import com.jayud.mall.model.bo.TaskQueryForm;
import com.jayud.mall.model.vo.TaskVO;
import com.jayud.mall.service.ITaskService;
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
@RequestMapping("/task")
@Api(tags = "A048-admin-基础任务接口")
@ApiSort(value = 48)
public class TaskController {

    @Autowired
    ITaskService taskService;

    //基础任务分页查询
    @ApiOperation(value = "基础任务分页查询")
        @PostMapping("/findTaskByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<TaskVO>> findTaskByPage(
            @RequestBody QueryTaskForm form) {
        IPage<TaskVO> pageList = taskService.findTaskByPage(form);
        CommonPageResult<TaskVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //查询基础任务list
    @ApiOperation(value = "查询基础任务list")
    @PostMapping("/findTask")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<TaskVO>> findTask(@RequestBody TaskQueryForm form){
        List<TaskVO> taskVOS = taskService.findTask(form);
        return CommonResult.success(taskVOS);
    }

    //保存基础任务
    @ApiOperation(value = "保存基础任务")
    @PostMapping("/saveTask")
    @ApiOperationSupport(order = 3)
    public CommonResult<TaskVO> saveTask(@Valid @RequestBody TaskForm form){
        return taskService.saveTask(form);
    }

    @ApiOperation(value = "根据id，查询基础任务")
    @PostMapping("/findTaskById")
    @ApiOperationSupport(order = 4)
    public CommonResult<TaskVO> findTaskById(@Valid @RequestBody TaskParaForm form){
        Long id = form.getId();
        return taskService.findTaskById(id);
    }



}
