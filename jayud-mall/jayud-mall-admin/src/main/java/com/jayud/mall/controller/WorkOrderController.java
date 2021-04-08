package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.bo.WorkOrderParaForm;
import com.jayud.mall.model.bo.WorkOrderReplyForm;
import com.jayud.mall.model.vo.WorkOrderVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWorkOrderService;
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
@RequestMapping("/workorder")
@Api(tags = "A052-admin-订单工单接口")
@ApiSort(value = 52)
public class WorkOrderController {

    @Autowired
    IWorkOrderService workOrderService;
    @Autowired
    BaseService baseService;

    //工单列表
    @ApiOperation(value = "工单列表")
    @PostMapping("/findWorkOrderByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<WorkOrderVO>> findWorkOrderByPage(@Valid @RequestBody QueryWorkOrderForm form) {
        IPage<WorkOrderVO> pageList = workOrderService.findWorkOrderByPage(form);
        CommonPageResult<WorkOrderVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //我的工单(我处理的工单)
    @ApiOperation(value = "我的工单(我处理的工单)")
    @PostMapping("/findMyWorkOrderByPage")
    @ApiOperationSupport(order = 2)
    public CommonResult<CommonPageResult<WorkOrderVO>> findMyWorkOrderByPage(@Valid @RequestBody QueryWorkOrderForm form) {
        AuthUser user = baseService.getUser();
        form.setOperator(user.getId());//我的工单
        IPage<WorkOrderVO> pageList = workOrderService.findWorkOrderByPage(form);
        CommonPageResult<WorkOrderVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //结单    statement
    @ApiOperation(value = "结单")
    @PostMapping("/statementWorkOrder")
    @ApiOperationSupport(order = 3)
    public CommonResult statementWorkOrder(@Valid @RequestBody WorkOrderParaForm form){
        Long id = form.getId();
        return workOrderService.statementWorkOrder(id);
    }

    //回复
    @ApiOperation(value = "回复")
    @PostMapping("/replyWorkOrder")
    @ApiOperationSupport(order = 4)
    public CommonResult replyWorkOrder(@Valid @RequestBody WorkOrderReplyForm form){
        return workOrderService.replyWorkOrder(form);
    }

    //TODO 工单分业务类型 1订单工单 2提单工单


}