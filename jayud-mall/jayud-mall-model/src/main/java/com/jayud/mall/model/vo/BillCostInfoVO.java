package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "BillCostVO", description = "提单费用信息VO")
@Data
public class BillCostInfoVO {

    @ApiModelProperty(value = "提单id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer supplierId;

    @ApiModelProperty(value = "提单对应应付费用list", position = 3)
    @JSONField(ordinal = 3)
    private List<BillCopePayVO> billCopePayForms;

    @ApiModelProperty(value = "提单对应应付费用合计(金额合计,格式化)", position = 4)
    @JSONField(ordinal = 4)
    private String billCopePayTotal;

}
