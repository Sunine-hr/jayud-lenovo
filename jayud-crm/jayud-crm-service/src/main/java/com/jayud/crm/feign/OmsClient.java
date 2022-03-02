package com.jayud.crm.feign;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @ApiOperation(value = "初始化车型尺寸,区分车型")
    @PostMapping(value = "/initVehicleSize")
    public CommonResult<List<Map<String, Object>>> initVehicleSize();

    /**
     * 币种
     *
     * @return
     */
    @RequestMapping(value = "api/initCurrencyInfo")
    ApiResult<List<InitComboxStrVO>> initCurrencyInfo();

    @ApiOperation(value = "录入费用:应收/付项目/币种 ")
    @PostMapping(value = "/initCost")
    public CommonResult initCost(@RequestBody Map<String, Object> param);

    @ApiOperation(value = "费用类别,idCode=费用名称的隐藏值")
    @PostMapping(value = "/initCostType")
    public CommonResult<List<InitComboxVO>> initCostType(@RequestBody Map<String, Object> param);

    @ApiOperation(value = "根据费用名称查询费用类型")
    @PostMapping(value = "/initCostTypeByCostInfoCode")
    public CommonResult<Map<String, List<InitComboxVO>>> initCostTypeByCostInfoCode();
}
