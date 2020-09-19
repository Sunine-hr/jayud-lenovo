package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.oms.model.bo.QueryOrderInfoForm;
import com.jayud.oms.model.vo.OrderInfoVO;
import com.jayud.oms.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orderInfo")
@Api(tags = "主订单接口")
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "未提交/提交订单列表")
    @PostMapping("/findOrderInfoByPage")
    public CommonResult<CommonPageResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    //1.订单详情 TODO

}

