package com.jayud.crm.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.crm.feign.OmsClient;
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
@RequestMapping("/common")
@Api(tags = "订单通用接口")
public class CommonController {

    @Autowired
    private OmsClient omsClient;

    /**
     * 币种
     *
     * @return
     */
    @ApiOperation(value = "币种")
    @PostMapping(value = "/initCostUnit")
    public CommonResult<List<InitComboxStrVO>> initCurrencyInfo() {
        return CommonResult.success(omsClient.initCurrencyInfo().getData());
    }


}

