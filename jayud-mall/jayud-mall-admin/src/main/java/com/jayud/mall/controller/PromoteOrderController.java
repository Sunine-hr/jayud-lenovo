package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PromoteOrderIdForm;
import com.jayud.mall.model.bo.QueryPromoteOrderForm;
import com.jayud.mall.model.bo.SavePromoteOrderForm;
import com.jayud.mall.model.vo.PromoteOrderVO;
import com.jayud.mall.service.IPromoteOrderService;
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
@RequestMapping("/promoteorder")
@Api(tags = "A057-admin-推广订单接口(客户列表)")
@ApiSort(value = 57)
public class PromoteOrderController {

    @Autowired
    IPromoteOrderService promoteOrderService;

    @ApiOperation(value = "1.分页查询推广订单page")
    @PostMapping("/findPromoteOrderByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<PromoteOrderVO>> findPromoteOrderByPage(@RequestBody QueryPromoteOrderForm form) {
        IPage<PromoteOrderVO> pageList = promoteOrderService.findPromoteOrderByPage(form);
        CommonPageResult<PromoteOrderVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "2.保存推广订单")
    @PostMapping("/savePromoteOrder")
    @ApiOperationSupport(order = 2)
    public CommonResult savePromoteOrder(@Valid @RequestBody SavePromoteOrderForm form){
        promoteOrderService.savePromoteOrder(form);
        return CommonResult.success("保存成功");
    }

    @ApiOperation(value = "3.查询推广订单")
    @PostMapping("/findPromoteOrderById")
    @ApiOperationSupport(order = 3)
    public CommonResult<PromoteOrderVO> findPromoteOrderById(@Valid @RequestBody PromoteOrderIdForm form){
        Integer id = form.getId();
        PromoteOrderVO promoteOrderVO = promoteOrderService.findPromoteOrderById(id);
        return CommonResult.success(promoteOrderVO);
    }

}
