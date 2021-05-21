package com.jayud.oms.feign;


import com.jayud.common.ApiResult;
import com.jayud.oms.model.po.CustomsFinanceFeeRelation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * customs模块消费finance模块的接口
 */
@FeignClient(value = "jayud-finance-web")
public interface FinanceClient {

    /**
     * 获取云报关-金蝶财务费用项对应关系
     */
    @RequestMapping(value = "/api/getCustomsFinanceFeeRelation")
    ApiResult<Map<String, CustomsFinanceFeeRelation>> getCustomsFinanceFeeRelation();

}
