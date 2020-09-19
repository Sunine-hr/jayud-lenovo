package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class AuditCostForm {

    @ApiModelProperty(value = "审核状态 1-通过 2-驳回", required = true)
    private String status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "应收费用", required = true)
    private List<Long> paymentIds;

    @ApiModelProperty(value = "应付费用", required = true)
    private List<Long> receivableIds;


}
