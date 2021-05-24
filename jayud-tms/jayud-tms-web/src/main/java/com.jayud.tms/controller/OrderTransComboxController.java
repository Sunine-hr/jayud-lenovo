package com.jayud.tms.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.CarTypeEnum;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.vo.DriverInfoLinkVO;
import com.jayud.tms.model.vo.InitComboxVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public CommonResult initSendCarCombox() {
        Map<String, Object> resultMap = new HashMap<>();
        List<InitComboxVO> supplierInfo = omsClient.initSupplierInfo().getData();
        List<InitComboxVO> warehouseInfo = omsClient.initWarehouseInfo().getData();
        resultMap.put(CommonConstant.SUPPLIERINFO, supplierInfo);
        resultMap.put(CommonConstant.WAREHOUSEINFO, warehouseInfo);
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "审核通过供应商")
    @PostMapping(value = "/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        List<InitComboxVO> supplierInfo = omsClient.initSupplierInfo().getData();
        return CommonResult.success(supplierInfo);
    }

    //TODO 从司机绑定多辆车到一部车绑多个司机
    @ApiOperation(value = "运输派车-司机姓名")
    @PostMapping(value = "/initDriver")
    public CommonResult<List<InitComboxVO>> initDriver() {
        List<InitComboxVO> driverInfo = omsClient.initDriver().getData();
        return CommonResult.success(driverInfo);
    }

//    @ApiOperation(value = "运输派车-司机姓名联动车辆供应商，大陆车牌，香港车牌，司机电话 id = 司机隐藏值")
//    @PostMapping(value = "/initDriverInfo")
//    public CommonResult<DriverInfoLinkVO> initDriverInfo(@RequestBody Map<String, Object> param) {
//        Long driverId = Long.valueOf(MapUtil.getStr(param, "id"));
//        DriverInfoLinkVO driverInfo = omsClient.initDriverInfo(driverId).getData();
//        return CommonResult.success(driverInfo);
//    }

    @ApiOperation(value = "运输派车页面-下拉车辆信息")
    @PostMapping(value = "/initVehicle")
    public CommonResult<List<InitComboxVO>> initVehicle() {
        List<InitComboxVO> initComboxVOS = omsClient.initVehicle(CarTypeEnum.ZERO.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "运输派车-大陆车牌联动车辆供应商，大陆车牌，香港车牌，司机电话 id = 车辆id")
    @PostMapping(value = "/initVehicleInfo")
    public CommonResult initVehicleInfo(@RequestBody Map<String, Object> param) {
        Long vehicleId = Long.valueOf(MapUtil.getStr(param, "id"));
        return CommonResult.success(omsClient.initVehicleInfo(vehicleId).getData());
    }

}

