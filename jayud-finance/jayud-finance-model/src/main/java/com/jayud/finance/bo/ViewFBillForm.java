package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 出应付对账单之前的暂存预览
 */
@Data
public class ViewFBillForm {

    @ApiModelProperty(value = "应付出账单界面部分")
    private List<OrderPaymentBillDetailForm> billDetailForms;


}
