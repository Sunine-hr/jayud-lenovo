package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ordercase")
@Api(tags = "订单对应箱号信息接口")
public class OrderCaseController {

    @Autowired
    IOrderCaseService orderCaseService;

    @ApiOperation(value = "分页查询订单对应箱号信息")
    @PostMapping("/findOrderCaseByPage")
    public CommonResult<CommonPageResult<OrderCaseVO>> findOrderCaseByPage(@RequestBody QueryOrderCaseForm form) {
        IPage<OrderCaseVO> pageList = orderCaseService.findOrderCaseByPage(form);
        CommonPageResult<OrderCaseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

}
