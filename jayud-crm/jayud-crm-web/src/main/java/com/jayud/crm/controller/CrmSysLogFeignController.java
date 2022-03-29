package com.jayud.crm.controller;


import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.QuerySysLogForm;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.service.ICrmCustomerFeaturesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Api(tags = "客户日志部分")
@RestController
@RequestMapping("/sysLogFeign")
public class CrmSysLogFeignController {

    @Autowired
    private AuthClient authClient;



    @ApiOperation("分页查询日志")
    @PostMapping("/selectSysLog")
    public BaseResult selectSysLog(@RequestBody QuerySysLogForm QuerySysLogForm) {

        return authClient.selectSysLogPageFeign(QuerySysLogForm);
    }

    @ApiOperation("公共方法新增日志")
    @PostMapping("/api/addSysLog")
    public BaseResult addSysLog(@RequestParam("logContent") String logContent,@RequestParam("businessId") Long businessId) {
        authClient.addSysLogFeign(logContent, businessId);
        return BaseResult.ok(SysTips.ADD_SUCCESS);
    }
}
