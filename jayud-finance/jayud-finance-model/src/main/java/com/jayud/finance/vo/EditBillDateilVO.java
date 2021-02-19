package com.jayud.finance.vo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应收/应付编辑对账单详情
 */
@Data
public class EditBillDateilVO {

    @ApiModelProperty(value = "账单编号,生成账单时必传")
    private String billNo;

    @ApiModelProperty(value = "核算期,生成账单时必传")
    private String accountTermStr;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "是否自定义汇率", required = true)
    private Boolean isCustomExchangeRate;

    @ApiModelProperty(value = "自定义汇率")
    private List<InitComboxStrVO> customExchangeRate;

    public void assembleCurrencyName(List<InitComboxStrVO> data) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        Map<String, String> currencyMap = data.stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        customExchangeRate.forEach(e -> e.setName(currencyMap.get(e.getCode())));
    }
}

