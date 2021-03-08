package com.jayud.trailer.controller;

import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.trailer.feign.OmsClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "拖车模块公用接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "下拉框(审核通过的供应商)")
    @PostMapping(value = "/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        CommonResult<List<InitComboxVO>> result = omsClient.initSupplierInfo();
//        if (result.getMsg().equals("成功")) {
//            log.warn("远程调用审核通过的供应商失败 msg={}", result.getMsg());
//            return CommonResult.error(ResultEnum.OPR_FAIL);
//        }
        return CommonResult.success(result.getData());
    }

    @ApiOperation(value = "订单列表-起运港、目的港，车型")
    @PostMapping(value = "/mainOrder/initSea")
    public CommonResult<Map<String, Object>> initTrailer() {

        //获取港口信息
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>) this.omsClient.initDictByDictTypeCode("Port").getData();

        //获取车型信息
        List<InitComboxVO> cabinetSizeInfo = (List<InitComboxVO>) this.omsClient.getVehicleSizeInfo().getData();
        Map map = new HashMap();
        map.put("portCodeInfo", portCodeInfo);
        map.put("cabinetSizeInfo", cabinetSizeInfo);
        return CommonResult.success(map);
    }

    @ApiOperation(value = "车辆下拉框")
    @PostMapping(value = "/mainOrder/initVehicleInfo")
    public CommonResult initVehicleInfo() {
        return CommonResult.success(omsClient.initVehicle().getData());
    }

    @ApiOperation(value = "车辆下拉框")
    @PostMapping(value = "/mainOrder/initDriverInfo")
    public CommonResult initDriverInfo(@RequestParam("vehicleId") Long vehicleId) {
        return CommonResult.success(omsClient.initVehicleInfo(vehicleId).getData());
    }

}
