package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.bo.WorkOrderAddForm;
import com.jayud.mall.model.bo.WorkOrderEvaluateForm;
import com.jayud.mall.model.bo.WorkOrderParaForm;
import com.jayud.mall.model.vo.WorkOrderVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
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
@Api(tags = "C019-client-工单接口")
@ApiSort(value = 19)
public class WorkOrderController {
    @Autowired
    IWorkOrderService workOrderService;
    @Autowired
    BaseService baseService;

    @ApiOperation(value = "工单分页查询")
    @PostMapping("/findWorkOrderByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<WorkOrderVO>> findWorkOrderByPage(@RequestBody QueryWorkOrderForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(customerUser.getId());
        IPage<WorkOrderVO> pageList = workOrderService.findWorkOrderByPage(form);
        CommonPageResult<WorkOrderVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "客户查看工单")
    @PostMapping("/findWorkOrderById")
    @ApiOperationSupport(order = 2)
    public CommonResult<WorkOrderVO> findWorkOrderById(@Valid @RequestBody WorkOrderParaForm form){
        Long id = form.getId();
        return workOrderService.findWorkOrderById(id);
    }

    @ApiOperation(value = "客户删除工单(仅关闭状态，可以删除)")
    @PostMapping("/delWorkOrderById")
    @ApiOperationSupport(order = 2)
    public CommonResult delWorkOrderById(@Valid @RequestBody WorkOrderParaForm form){
        Long id = form.getId();
        return workOrderService.delWorkOrderById(id);
    }


    @ApiOperation(value = "客户评价工单(仅待评价状态，可以评价)")
    @PostMapping("/evaluateWorkOrderById")
    @ApiOperationSupport(order = 3)
    public CommonResult evaluateWorkOrderById(@Valid @RequestBody WorkOrderEvaluateForm form){
        return workOrderService.evaluateWorkOrderById(form);
    }

    //客户新增工单
    @ApiOperation(value = "客户新增工单")
    @PostMapping("/addWorkOrder")
    @ApiOperationSupport(order = 4)
    public CommonResult<WorkOrderVO> addWorkOrder(@Valid @RequestBody WorkOrderAddForm form){
        return workOrderService.addWorkOrder(form);
    }



}
