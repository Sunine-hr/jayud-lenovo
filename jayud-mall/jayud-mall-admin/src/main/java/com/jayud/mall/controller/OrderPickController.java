package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryOrderPickForm;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.service.IOrderPickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderpick")
@Api(tags = "A054-admin-订单对应提货信息表接口(提货管理)")
@ApiSort(value = 54)
public class OrderPickController {

    @Autowired
    IOrderPickService orderPickService;

    @ApiOperation(value = "订单提货分页查询")
    @PostMapping("/findOrderPickByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OrderPickVO>> findOrderPickByPage(
            @RequestBody QueryOrderPickForm form) {
        IPage<OrderPickVO> pageList = orderPickService.findOrderPickByPage(form);
        CommonPageResult<OrderPickVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
