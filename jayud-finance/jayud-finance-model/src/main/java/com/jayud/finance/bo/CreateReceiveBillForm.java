package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 *暂存/提交对账单
 */
@Data
public class CreateReceiveBillForm {

    @ApiModelProperty(value = "应收出账单界面部分,生成账单时必传")
    private OrderReceiveBillForm receiveBillForm;

    @Valid
    @ApiModelProperty(value = "应收出账单详情界面部分",required = true)
    private List<OrderReceiveBillDetailForm> receiveBillDetailForms;

    @ApiModelProperty(value = "账单编号,生成账单时必传")
    private String billNo;

    @ApiModelProperty(value = "开始核算期,生成账单时必传")
    private String beginAccountTermStr;

    @ApiModelProperty(value = "结束核算期,生成账单时必传")
    private String endAccountTermStr;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "账单类别,生成账单时必传，只允许填写main or zgys or bg")
    private String subType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单",required = true)
    @Pattern(regexp = "(pre_create|create)", message = "只允许填写pre_create or create")
    private String cmd;

}
