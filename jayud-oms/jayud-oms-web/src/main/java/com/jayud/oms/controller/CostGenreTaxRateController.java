package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.po.CostGenreTaxRate;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.service.ICostGenreTaxRateService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 费用类型税率表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-09-02
 */
@RestController
@RequestMapping("/costGenreTaxRate")
public class CostGenreTaxRateController {

    @Autowired
    private ICostGenreTaxRateService costGenreTaxRateService;

    @ApiOperation("根据费用类型和作业环节查询税率")
    @PostMapping("/getTaxRate")
    public CommonResult<BigDecimal> getTaxRate(@RequestBody Map<String, Object> map) {
        Long costGenreId = MapUtil.getLong(map, "costGenreId");
        Long costTypeId = MapUtil.getLong(map, "costTypeId");
        if (costGenreId == null || costTypeId == null) {
            return CommonResult.error(400, "请选择作业环节/费用类型");
        }
        List<CostGenreTaxRate> list = this.costGenreTaxRateService.list(new QueryWrapper<>(new CostGenreTaxRate().setCostGenreId(costGenreId).setCostTypeId(costTypeId)));
        if (CollectionUtils.isEmpty(list)) {
            return CommonResult.success(new BigDecimal(0));
        } else {
            return CommonResult.success(list.get(0).getTaxRate());
        }
    }
}

