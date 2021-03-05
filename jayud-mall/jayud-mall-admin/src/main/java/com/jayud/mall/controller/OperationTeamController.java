package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.model.bo.OperationTeamParaForm;
import com.jayud.mall.model.bo.QueryOperationTeamForm;
import com.jayud.mall.model.vo.OperationTeamVO;
import com.jayud.mall.service.IOperationTeamService;
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
@RequestMapping("/operationteam")
@Api(tags = "A019-admin-运营(服务)小组接口")
@ApiSort(value = 19)
public class OperationTeamController {

    @Autowired
    IOperationTeamService operationTeamService;

    @ApiOperation(value = "查询-运营(服务)小组list")
    @PostMapping(value = "/findOperationTeam")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OperationTeamVO>> findOperationTeam(@RequestBody OperationTeamForm form) {
        List<OperationTeamVO> operationTeams = operationTeamService.findOperationTeam(form);
        return CommonResult.success(operationTeams);
    }

    @ApiOperation(value = "分页查询运营服务小组")
    @PostMapping("/findOperationTeamByPage")
    @ApiOperationSupport(order = 2)
    public CommonResult<CommonPageResult<OperationTeamVO>> findOperationTeamByPage(
            @RequestBody QueryOperationTeamForm form) {
        IPage<OperationTeamVO> pageList = operationTeamService.findOperationTeamByPage(form);
        CommonPageResult<OperationTeamVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //保存运营服务小组以及关联的小组人员 member_user_id
    @ApiOperation(value = "保存运营服务小组以及关联的小组人员")
    @PostMapping("/saveOperationTeam")
    @ApiOperationSupport(order = 3)
    public CommonResult<OperationTeamVO> saveOperationTeam(@Valid @RequestBody OperationTeamForm form){
        return operationTeamService.saveOperationTeam(form);
    }

    //根据id，获取运营服务小组以及关联的小组人员
    @ApiOperation(value = "根据id，获取任务组以及任务组关联的任务项")
    @PostMapping("/findOperationTeamById")
    @ApiOperationSupport(order = 4)
    public CommonResult<OperationTeamVO> findOperationTeamById(@Valid @RequestBody OperationTeamParaForm form){
        Long id = form.getId();
        return operationTeamService.findOperationTeamById(id);
    }






}
