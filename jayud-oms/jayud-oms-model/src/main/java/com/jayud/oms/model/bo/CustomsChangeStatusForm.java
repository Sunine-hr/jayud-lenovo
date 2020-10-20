package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class CustomsChangeStatusForm {

    @ApiModelProperty(value = "子订单号")
    private List<String> orderNos;

    @ApiModelProperty(value = "状态")
    private String status;


}
