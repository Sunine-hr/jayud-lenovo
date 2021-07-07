package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "BillCustomsInfoQueryForm", description = "提单下的报关清单")
@Data
public class BillCustomsInfoQueryForm {

    @ApiModelProperty(value = "(提单)报关信息表id(bill_customs_info id)")
    @NotNull(message = "(提单)报关信息表id不能为空")
    private Long id;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "类型(0买单 1独立)")
    private Integer type;

    @ApiModelProperty(value = "过滤 箱号id(order_case id) list")
    private List<Long> filterCaseIds;
}
