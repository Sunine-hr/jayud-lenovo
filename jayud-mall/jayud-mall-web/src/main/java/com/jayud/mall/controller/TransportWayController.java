package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TransportWayForm;
import com.jayud.mall.model.vo.TransportWayVO;
import com.jayud.mall.service.ITransportWayService;
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
@RequestMapping("/transportway")
@Api(tags = "C009-C端-运输方式接口")
@ApiSort(value = 9)
public class TransportWayController {

    @Autowired
    ITransportWayService transportWayService;

    @ApiOperation(value = "查询运输方式List")
    @PostMapping("/findTransportWay")
    @ApiOperationSupport(order = 1)
    public CommonResult<List<TransportWayVO>> findTransportWay(@RequestBody TransportWayForm form) {
        List<TransportWayVO> list = transportWayService.findTransportWay(form);
        return CommonResult.success(list);
    }



}
