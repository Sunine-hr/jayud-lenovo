package com.jayud.airfreight.controller;

import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.IAirPortService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.Result;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "空运模块公用接口")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IAirPortService airPortService;

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

    @ApiOperation(value = "主订单下拉选项-飞机港口,贸易类型")
    @PostMapping(value = "/mainOrder/initAir")
    public CommonResult<Map<String, Object>> initAir() {
        List<InitComboxStrVO> initComboxStrVOS = this.airPortService.initAirPort();
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> terms = new ArrayList<>();
        //贸易类型
        for (AirOrderTermsEnum termsEnum : AirOrderTermsEnum.values()) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", termsEnum.getCode());
            tmp.put("name", termsEnum.getDesc());
            terms.add(tmp);
        }
        //空运港口下拉选项
        response.put("airPorts", initComboxStrVOS);
        //贸易类型下拉选项
        response.put("airTerms", terms);
        return CommonResult.success(response);
    }
}
