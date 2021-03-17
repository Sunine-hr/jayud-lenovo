package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryAccountReceivableForm extends BasePageForm {

    //对账单编号
    @ApiModelProperty(value = "对账单编号", position = 1)
    @JSONField(ordinal = 1)
    private String dzdNo;

    //法人主体
    @ApiModelProperty(value = "法人id(legal_person id)", position = 2)
    @JSONField(ordinal = 2)
    private Long legalPersonId;

    //客户名称
    @ApiModelProperty(value = "客户ID(customer id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer customerId;

    //费用状态
    @ApiModelProperty(value = "状态(0待核销 1核销完成)", position = 4)
    @JSONField(ordinal = 4)
    private Integer status;

}
