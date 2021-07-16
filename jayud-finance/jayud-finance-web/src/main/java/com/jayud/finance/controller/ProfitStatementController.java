package com.jayud.finance.controller;


import com.jayud.common.CommonResult;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.bo.QueryProfitStatementForm;
import com.jayud.finance.po.ProfitStatement;
import com.jayud.finance.service.IProfitStatementService;
import com.jayud.finance.vo.ProfitStatementBillVO;
import com.jayud.finance.vo.ProfitStatementVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 利润报表 前端控制器
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@RestController
@RequestMapping("/profitStatement")
public class ProfitStatementController {

    @Autowired
    private IProfitStatementService profitStatementService;

    @ApiOperation(value = "利润报表查询")
    @PostMapping("list")
    private CommonResult<Map<String, Object>> list(@RequestBody QueryProfitStatementForm form) {
        Map<String, Object> rollCallback = new HashMap<>();
        List<ProfitStatementVO> list = this.profitStatementService.list(form, rollCallback);
        rollCallback.put("list", list);
        return CommonResult.success(rollCallback);
    }


    @ApiOperation(value = "查询账单")
    @PostMapping("getProfitStatementBill")
    private CommonResult<ProfitStatementBillVO> getProfitStatementBill(@RequestBody ProfitStatementVO form) {
        String reCostIdStr = form.getIsOpenInternal() ? form.getReInCostIds() : form.getReCostIds();
        String payCostIdStr = form.getIsOpenInternal() ? form.getPayInCostIds() : form.getPayCostIds();
        if (StringUtils.isEmpty(reCostIdStr) || StringUtils.isEmpty(payCostIdStr)) {
            return CommonResult.error(400, "没有找到对应账单");
        }
        String[] reCostIds = reCostIdStr.split(",");
        String[] payCostIds = payCostIdStr.split(",");
        ProfitStatementBillVO billVO = this.profitStatementService.getProfitStatementBill(Arrays.asList(reCostIds), Arrays.asList(payCostIds));
        return CommonResult.success(billVO);
    }

    @ApiOperation(value = "查询费用明细")
    @PostMapping("getCostDetails")
    private CommonResult<ProfitStatementBillVO> getCostDetails(@RequestBody ProfitStatementVO form) {
        String reCostIdStr = form.getIsOpenInternal() ? form.getReInCostIds() : form.getReCostIds();
        String payCostIdStr = form.getIsOpenInternal() ? form.getPayInCostIds() : form.getPayCostIds();
        if (StringUtils.isEmpty(reCostIdStr) || StringUtils.isEmpty(payCostIdStr)) {
            return CommonResult.error(400, "没有找到对应费用明细");
        }
        String[] reCostIds = reCostIdStr.split(",");
        String[] payCostIds = payCostIdStr.split(",");
        ProfitStatementBillVO billVO = this.profitStatementService.getCostDetails(Arrays.asList(reCostIds), Arrays.asList(payCostIds));
        return CommonResult.success(billVO);
    }

}

