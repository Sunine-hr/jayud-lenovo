package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryTaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupForm;
import com.jayud.mall.model.bo.TaskGroupParaForm;
import com.jayud.mall.model.bo.TaskGroupQueryForm;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/taskgroup")
@Api(tags = "A032-admin-任务分组接口")
@ApiSort(value = 32)
public class TaskGroupController {

    @Autowired
    ITaskGroupService taskGroupService;

    @ApiOperation(value = "查询任务分组List")
    @PostMapping("/findTaskGroup")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TaskGroupVO>> findTaskGroup(@RequestBody TaskGroupQueryForm form) {
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

    @ApiOperation(value = "分页查询任务分组")
    @PostMapping("/findTaskGroupByPage")
    @ApiOperationSupport(order = 4)
    public CommonResult<CommonPageResult<TaskGroupVO>> findTaskGroupByPage(
            @RequestBody QueryTaskGroupForm form) {
        IPage<TaskGroupVO> pageList = taskGroupService.findTaskGroupByPage(form);
        CommonPageResult<TaskGroupVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //保存任务组以及任务组关联的任务项
    @ApiOperation(value = "保存任务组以及任务组关联的任务项")
    @PostMapping("/saveTaskGroup")
    @ApiOperationSupport(order = 5)
    public CommonResult<TaskGroupVO> saveTaskGroup(@Valid @RequestBody TaskGroupForm form){
        return taskGroupService.saveTaskGroup(form);
    }

    //根据id，获取任务组以及任务组关联的任务项
    @ApiOperation(value = "根据id，获取任务组以及任务组关联的任务项")
    @PostMapping("/findTaskGroupById")
    @ApiOperationSupport(order = 6)
    public CommonResult<TaskGroupVO> findTaskGroupById(@Valid @RequestBody TaskGroupParaForm form){
        Long id = form.getId();
        return taskGroupService.findTaskGroupById(id);
    }




}
