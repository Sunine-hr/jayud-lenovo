package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ConfCaseVO", description = "配载的箱号")
public class ConfCaseVO {

    @ApiModelProperty(value = "箱号id")
    private Long caseId;
    @ApiModelProperty(value = "箱号")
    private String cartonNo;
    @ApiModelProperty(value = "客户测量的重量，单位kg")
    private BigDecimal asnWeight;
    @ApiModelProperty(value = "预报长宽高计算得到的体积，单位m³")
    private BigDecimal asnVolume;
    @ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)")
    private Integer needDeclare;
    @ApiModelProperty(value = "是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)")
    private Integer needClearance;
    @ApiModelProperty(value = "客户ID(customer id)")
    private Integer customerId;
    @ApiModelProperty(value = "客户名称")
    private String customerName;


}
