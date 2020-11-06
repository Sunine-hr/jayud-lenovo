package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 账单预览
 */
@Data
public class ViewBillVO {

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "开始核算期")
    private String beginAccountTermStr;

    @ApiModelProperty(value = "结束核算期")
    private String endAccountTermStr;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "制单人")
    private String makeUser;

    @ApiModelProperty(value = "制单时间")
    private String makeTimeStr;
}
