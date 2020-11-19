package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 出应收对账单之前的暂存预览
 */
@Data
public class ViewSBillForm {

    @ApiModelProperty(value = "应收出账单界面部分")
    private List<OrderReceiveBillDetailForm> billDetailForms;


}
