package com.jayud.tms.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.vo.InitComboxVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/combox")
@Api(tags = "初始化下拉框接口")
public class OrderTransComboxController {


    @Autowired
    OmsClient omsClient;

    @ApiOperation(value = "中转仓库和车辆供应商")
    @PostMapping(value = "/initSendCarCombox")
    public CommonResult initWarehouseInfo() {
        Map<String,Object> resultMap = new HashMap<>();
        List<InitComboxVO> supplierInfo = omsClient.initSupplierInfo().getData();
        List<InitComboxVO> warehouseInfo = omsClient.initWarehouseInfo().getData();
        resultMap.put(CommonConstant.SUPPLIERINFO,supplierInfo);
        resultMap.put(CommonConstant.WAREHOUSEINFO,warehouseInfo);
        return CommonResult.success(resultMap);
    }






}

