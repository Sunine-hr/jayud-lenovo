package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BillAmountTotalVO {

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "总计")
    private String total;


    public BillAmountTotalVO() {
    }

    public BillAmountTotalVO(String currency, String total) {
        this.currency = currency;
        this.total = total;
    }
}
