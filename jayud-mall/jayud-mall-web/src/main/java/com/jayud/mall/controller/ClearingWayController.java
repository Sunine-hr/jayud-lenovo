package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.vo.ClearingWayVO;
import com.jayud.mall.service.IClearingWayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C022-client-结算方式接口")
@ApiSort(value = 22)
@RestController
@RequestMapping("/clearingway")
public class ClearingWayController {

    @Autowired
    IClearingWayService clearingWayService;

    @ApiOperation(value = "查询结算方式list")
    @ApiOperationSupport(order = 1)
    @PostMapping("/findClearingWay")
    public CommonResult<List<ClearingWayVO>> findClearingWay(){
        List<ClearingWayVO> clearingWayList = clearingWayService.findClearingWay();
        return CommonResult.success(clearingWayList);
    }

}
