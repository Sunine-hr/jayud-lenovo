package com.jayud.finance.controller;

import com.jayud.common.ApiResult;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.po.CustomsFinanceFeeRelation;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.service.PreloadService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@Api(tags = "finance对外接口")
@Slf4j
public class ExternalApiController {
    @Autowired
    PreloadService preloadService;
    @Autowired
    private ICurrencyRateService currencyRateService;

    /**
     * 获取云报关-金蝶财务费用项对应关系
     *
     * @return
     */
    @RequestMapping(value = "/api/getCustomsFinanceFeeRelation")
    public ApiResult getCustomsFinanceFeeRelation() {
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = preloadService.getFeeRelationMap();
        return ApiResult.ok(feeRelationMap);
    }

    /**
     * 根据原始币种和兑换币种获取汇率
     *
     * @return
     */
    @RequestMapping(value = "/api/getExchangeRates")
    public ApiResult<Map<String, BigDecimal>> getExchangeRates(@RequestParam("dcCode") String dcCode,
                                                               @RequestParam("month") String month) {
        month = month.length() > 7 ? month.substring(0, 7) : month;//把时间处理到月
        return ApiResult.ok(this.currencyRateService.getExchangeRates(dcCode, month));
    }


}
