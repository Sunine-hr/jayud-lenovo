package com.jayud.finance.controller;

import com.jayud.common.ApiResult;
import com.jayud.finance.po.CustomsFinanceFeeRelation;
import com.jayud.finance.service.PreloadService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api(tags = "finance对外接口")
@Slf4j
public class ExternalApiController {
    @Autowired
    PreloadService preloadService;
    @Autowired


    /**
     * 获取云报关-金蝶财务费用项对应关系
     * @return
     */
    @RequestMapping(value = "/api/getCustomsFinanceFeeRelation")
    public ApiResult getCustomsFinanceFeeRelation() {
        Map<String, CustomsFinanceFeeRelation> feeRelationMap = preloadService.getFeeRelationMap();
        return ApiResult.ok(feeRelationMap);
    }
}
