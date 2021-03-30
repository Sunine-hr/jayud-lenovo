package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkBillForm;
import com.jayud.mall.model.bo.WorkBillParaForm;
import com.jayud.mall.model.bo.WorkBillReplyForm;
import com.jayud.mall.model.vo.WorkBillVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWorkBillService;
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

@RestController
@RequestMapping("/workbill")
@Api(tags = "A053-admin-提单工单接口")
@ApiSort(value = 53)
public class WorkBillController {

    @Autowired
    IWorkBillService workBillService;
    @Autowired
    BaseService baseService;

    //工单列表
    @ApiOperation(value = "工单列表")
    @PostMapping("/findWorkBillByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<WorkBillVO>> findWorkBillByPage(@RequestBody QueryWorkBillForm form) {
        IPage<WorkBillVO> pageList = workBillService.findWorkBillByPage(form);
        CommonPageResult<WorkBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //我的工单(我处理的工单)
    @ApiOperation(value = "我的工单(我处理的工单)")
    @PostMapping("/findMyWorkBillByPage")
    @ApiOperationSupport(order = 2)
    public CommonResult<CommonPageResult<WorkBillVO>> findMyWorkBillByPage(@RequestBody QueryWorkBillForm form) {
        AuthUser user = baseService.getUser();
        form.setOperator(user.getId());//我的工单
        IPage<WorkBillVO> pageList = workBillService.findWorkBillByPage(form);
        CommonPageResult<WorkBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //结单    statement
    @ApiOperation(value = "结单")
    @PostMapping("/statementWorkBill")
    @ApiOperationSupport(order = 3)
    public CommonResult statementWorkBill(@Valid @RequestBody WorkBillParaForm form){
        Long id = form.getId();
        return workBillService.statementWorkBill(id);
    }

    //回复
    @ApiOperation(value = "回复")
    @PostMapping("/replyWorkBill")
    @ApiOperationSupport(order = 4)
    public CommonResult replyWorkBill(@Valid @RequestBody WorkBillReplyForm form){
        return workBillService.replyWorkBill(form);
    }

}
