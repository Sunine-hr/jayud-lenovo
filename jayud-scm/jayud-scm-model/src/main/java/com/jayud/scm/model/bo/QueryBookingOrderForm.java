package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryBookingOrderForm extends BasePageForm {

    //委托单日期

    //确认报关日期

    //订车日期

    //审核状态

    //查询条件
    //委托单号
    //客户名称
    //报关单号

    //委托单状态

    @ApiModelProperty(value = "委托单号")
    private String bookingNo;

    @ApiModelProperty(value = "业务类型/工作单类型 0进口  1出口 2国内 4香港  5采购  6销售")
    private Integer modelType;

}
