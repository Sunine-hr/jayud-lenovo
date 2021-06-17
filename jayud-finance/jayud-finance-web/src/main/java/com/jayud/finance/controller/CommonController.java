package com.jayud.finance.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.vo.OrderReceiveBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
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


    @ApiOperation(value = "计算费用")
    @PostMapping("/calculatingCosts")
    public CommonResult<Map<String, String>> findReceiveBillByPage(@RequestBody Map<String, Object> map) {
        List<String> amountStrs = MapUtil.get(map, "amountStrs", List.class);
        Map<String, String> response = new HashMap<>();
        response.put("totalCost", "0");
        if (CollectionUtils.isEmpty(amountStrs)) {
            return CommonResult.success(response);
        }
        Map<String, BigDecimal> cost = new HashMap<>();
        for (String amount : amountStrs) {
            if (amount.contains(",")) {
                String[] split = amount.split(",");
                for (int i = 0; i < split.length; i++) {
                    String[] tmp = split[i].split(" ");
                    String currencyName = tmp[1];
                    cost.merge(currencyName, new BigDecimal(tmp[0]), BigDecimal::add);
                }
            } else {
                String[] split = amount.split(" ");
                String currencyName = split[1];
                cost.merge(currencyName, new BigDecimal(split[0]), BigDecimal::add);
            }

        }
        //返回合计的费用
        StringBuilder sb = new StringBuilder();
        cost.forEach((k, v) -> sb.append(v).append(" ").append(k).append(","));
        response.put("totalCost", sb.toString());
        return CommonResult.success(response);
    }
}
