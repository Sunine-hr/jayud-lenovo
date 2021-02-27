package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BillCostInfoForm {

    @ApiModelProperty(value = "提单id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "提单id不能为空")
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "供应商id不能为空")
    private Integer supplierId;

    @ApiModelProperty(value = "提单对应应付费用list", position = 3)
    @JSONField(ordinal = 3)
    @NotEmpty(message = "提单对应应付费用list不能为空")
    private List<BillCopePayForm> billCopePayForms;

    @ApiModelProperty(value = "提单对应应付费用合计(金额合计,格式化)", position = 4)
    @JSONField(ordinal = 4)
    private String billCopePayTotal;

}