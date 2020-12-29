package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TaskMemberRelationForm;
import com.jayud.mall.model.vo.TaskMemberRelationVO;
import com.jayud.mall.service.ITaskMemberRelationService;
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
@RequestMapping("/taskmemberrelation")
@Api(tags = "S020-后端-任务成员关系表接口")
@ApiSort(value = 20)
public class TaskMemberRelationController {

    @Autowired
    ITaskMemberRelationService taskMemberRelationService;

    @ApiOperation(value = "查看-任务成员关系表list")
    @PostMapping(value = "/findTaskMemberRelation")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TaskMemberRelationVO>> findTaskMemberRelation(@RequestBody TaskMemberRelationForm form){
        List<TaskMemberRelationVO> taskMemberRelationVOS = taskMemberRelationService.findTaskMemberRelation(form);
        return CommonResult.success(taskMemberRelationVOS);
    }

}
