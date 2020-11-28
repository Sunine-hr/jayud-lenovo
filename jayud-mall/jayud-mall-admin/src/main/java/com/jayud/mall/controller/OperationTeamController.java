package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OperationTeamForm;
import com.jayud.mall.model.vo.OperationTeamVO;
import com.jayud.mall.service.IOperationTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operationteam")
@Api(tags = "后端-运营(服务)小组-接口")
public class OperationTeamController {

    @Autowired
    IOperationTeamService operationTeamService;

    @ApiOperation(value = "查询-运营(服务)小组list")
    @PostMapping(value = "/findOperationTeam")
    public CommonResult<List<OperationTeamVO>> findOperationTeam(@RequestBody OperationTeamForm form) {
        List<OperationTeamVO> operationTeams = operationTeamService.findOperationTeam(form);
        return CommonResult.success(operationTeams);
    }




}
