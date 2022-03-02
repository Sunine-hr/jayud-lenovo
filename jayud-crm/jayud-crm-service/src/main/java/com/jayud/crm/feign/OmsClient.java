package com.jayud.crm.feign;


import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;


/**
 * tms模块消费oms模块的接口
 */
@FeignClient(value = "jayud-oms-web")
public interface OmsClient {


    @ApiOperation(value = "获取启用费用名称")
    @PostMapping(value = "/getCostInfos")
    public CommonResult<List<Map<String, Object>>> getCostInfos();

    @ApiOperation(value = "获取启用费用类型")
    @PostMapping(value = "/getCostTypes")
    public CommonResult<List<Map<String, Object>>> getCostTypes();


}
