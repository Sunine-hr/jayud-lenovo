package com.jayud.airfreight.controller;

import com.jayud.airfreight.feign.OmsClient;
import com.jayud.common.CommonResult;
import com.jayud.common.Result;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.ResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "空运模块公用接口")
@RestController
@Slf4j
public class CommonController {

    @Autowired
    private OmsClient omsClient;

    @ApiOperation(value = "下拉框(审核通过的供应商)")
    @PostMapping(value = "/api/initSupplierInfo")
    public CommonResult initSupplierInfo() {
        if (omsClient.initSupplierInfo().getCode() != HttpStatus.SC_OK) {
            log.warn("远程调用审核通过的供应商");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }
}
