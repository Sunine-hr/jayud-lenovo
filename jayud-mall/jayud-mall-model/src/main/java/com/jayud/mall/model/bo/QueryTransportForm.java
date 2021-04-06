package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryTransportForm extends BasePageForm {

    //关键字(订单编号、提货单号)
    @ApiModelProperty(value = "关键字(运输单号,追踪号)")
    private String keyword;

}
