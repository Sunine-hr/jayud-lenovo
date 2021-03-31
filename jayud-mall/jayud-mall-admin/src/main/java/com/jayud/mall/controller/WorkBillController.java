package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
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

    //1.后台用户管理人员的操作

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


    //2.后台用户创建人的操作

    @ApiOperation(value = "后台用户分页查询工单(查询自己创建的工单)")
    @PostMapping("/findOneselfWorkBillByPage")
    @ApiOperationSupport(order = 5)
    public CommonResult<CommonPageResult<WorkBillVO>> findOneselfWorkBillByPage(@RequestBody QueryWorkBillForm form) {
        AuthUser user = baseService.getUser();
        form.setCreateId(user.getId().intValue());
        IPage<WorkBillVO> pageList = workBillService.findWorkBillByPage(form);
        CommonPageResult<WorkBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查看工单")
    @PostMapping("/findWorkBillById")
    @ApiOperationSupport(order = 6)
    public CommonResult<WorkBillVO> findWorkBillById(@Valid @RequestBody WorkBillParaForm form){
        Long id = form.getId();
        return workBillService.findWorkBillById(id);
    }

    @ApiOperation(value = "后台用户删除工单(仅关闭状态，可以删除)")
    @PostMapping("/delWorkBillById")
    @ApiOperationSupport(order = 7)
    public CommonResult delWorkBillById(@Valid @RequestBody WorkBillParaForm form){
        Long id = form.getId();
        return workBillService.delWorkBillById(id);
    }

    @ApiOperation(value = "后台用户评价工单(仅待评价状态，可以评价)")
    @PostMapping("/evaluateWorkBillById")
    @ApiOperationSupport(order = 8)
    public CommonResult evaluateWorkBillById(@Valid @RequestBody WorkBillEvaluateForm form){
        return workBillService.evaluateWorkBillById(form);
    }

    //用户新增工单
    @ApiOperation(value = "后台用户新增工单")
    @PostMapping("/addWorkBill")
    @ApiOperationSupport(order = 9)
    public CommonResult<WorkBillVO> addWorkBill(@Valid @RequestBody WorkBillAddForm form){
        return workBillService.addWorkBill(form);
    }

    //用户关闭工单
    @ApiOperation(value = "后台用户关闭工单")
    @PostMapping("/closeWorkBill")
    @ApiOperationSupport(order = 10)
    public CommonResult closeWorkBill(@Valid @RequestBody WorkBillParaForm form){
        Long id = form.getId();
        return workBillService.closeWorkBill(id);
    }

}
