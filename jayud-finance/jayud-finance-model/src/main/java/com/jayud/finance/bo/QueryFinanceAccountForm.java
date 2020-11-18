package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 财务核算查询
 */
@Data
public class QueryFinanceAccountForm extends BasePageForm{

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "业务员")
    private String ywName;

    @ApiModelProperty(value = "应收对账单状态")
    private String sStatus;

    @ApiModelProperty(value = "应收费用状态")
    private String sCostStatus;

    @ApiModelProperty(value = "应付费用状态")
    private String fCostStatus;

    @ApiModelProperty(value = "应付对账单状态")
    private String fStatus;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "开始核算期,暂不做")
    private String beginAccountTermStr;

    @ApiModelProperty(value = "结束核算期,暂不做")
    private String endAccountTermStr;

}
