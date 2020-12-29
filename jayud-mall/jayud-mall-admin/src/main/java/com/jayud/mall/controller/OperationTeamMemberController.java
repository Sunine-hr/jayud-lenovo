package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.OperationTeamMemberForm;
import com.jayud.mall.model.vo.OperationTeamMemberVO;
import com.jayud.mall.service.IOperationTeamMemberService;
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
@RequestMapping("/operationteammember")
@Api(tags = "S019-后端-运营(服务)小组成员接口")
@ApiSort(value = 19)
public class OperationTeamMemberController {

    @Autowired
    IOperationTeamMemberService operationTeamMemberService;

    @ApiOperation(value = "查询-运营(服务)小组成员")
    @PostMapping(value = "/findOperationTeamMember")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<OperationTeamMemberVO>> findOperationTeamMember(@RequestBody OperationTeamMemberForm form){
        List<OperationTeamMemberVO> operationTeamMemberVOS = operationTeamMemberService.findOperationTeamMember(form);
        return CommonResult.success(operationTeamMemberVOS);
    }

}
