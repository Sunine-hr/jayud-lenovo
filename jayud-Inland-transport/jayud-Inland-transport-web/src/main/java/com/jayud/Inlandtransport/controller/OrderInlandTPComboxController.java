package com.jayud.Inlandtransport.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.CarTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/combox")
@Api(tags = "初始化下拉框接口")
public class OrderInlandTPComboxController {


    @Autowired
    OmsClient omsClient;

//    @ApiOperation(value = "中转仓库和车辆供应商")
//    @PostMapping(value = "/initSendCarCombox")
//    public CommonResult initWarehouseInfo() {
//        Map<String, Object> resultMap = new HashMap<>();
//        List<InitComboxVO> supplierInfo = omsClient.initSupplierInfo().getData();
//        List<InitComboxVO> warehouseInfo = omsClient.initWarehouseInfo().getData();
//        resultMap.put(CommonConstant.SUPPLIERINFO, supplierInfo);
//        resultMap.put(CommonConstant.WAREHOUSEINFO, warehouseInfo);
//        return CommonResult.success(resultMap);
//    }

    @ApiOperation(value = "运输派车页面-下拉车辆信息")
    @PostMapping(value = "/initVehicle")
    public CommonResult<List<InitComboxVO>> initVehicle() {
        List<InitComboxVO> initComboxVOS = omsClient.initVehicle(CarTypeEnum.ONE.getCode()).getData();
        return CommonResult.success(initComboxVOS);
    }

    @ApiOperation(value = "运输派车-大陆车牌联动车辆供应商，大陆车牌，香港车牌，司机电话 id = 车辆id")
    @PostMapping(value = "/initVehicleInfo")
    public CommonResult initVehicleInfo(@RequestBody Map<String, Object> param) {
        Long vehicleId = Long.valueOf(MapUtil.getStr(param, "id"));
        return CommonResult.success(omsClient.initVehicleInfo(vehicleId).getData());
    }

    @ApiOperation(value = "根据手机号获取联系人信息下拉选项")
    @PostMapping(value = "/getContactInfoByPhone")
    public CommonResult getContactInfoByPhone() {
        return CommonResult.success(omsClient.getContactInfoByPhone(BusinessTypeEnum.NL.getCode()).getData());
    }

}

