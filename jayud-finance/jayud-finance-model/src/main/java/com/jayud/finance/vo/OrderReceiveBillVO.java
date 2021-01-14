package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 该视图从其他表查询统计而来展示，后在保存到表中
 */
@Data
public class OrderReceiveBillVO {

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "已出账金额")
    private String alreadyPaidAmount;

    @ApiModelProperty(value = "已出账订单数")
    private Integer billOrderNum;

    @ApiModelProperty(value = "账单数")
    private Integer billNum;

    @ApiModelProperty(value = "未出账金额")
    private String notPaidAmount;

    @ApiModelProperty(value = "未出账订单数")
    private Integer notPaidOrderNum;


}
