package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.vo.OrderReceiveBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common/")
@Api(tags = "公共方法")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "计算费用")
    @PostMapping("/calculatingCosts")
    public CommonResult<Map<String, String>> calculatingCosts(@RequestBody Map<String, Object> map) {
        List<String> amountStrs = MapUtil.get(map, "amountStrs", List.class);
        Map<String, String> response = new HashMap<>();
        response.put("totalCost", "0");
        if (CollectionUtils.isEmpty(amountStrs)) {
            return CommonResult.success(response);
        }
        String totalCostStr=this.commonService.calculatingCosts(amountStrs);
        response.put("totalCost", totalCostStr);
        return CommonResult.success(response);
    }
}
