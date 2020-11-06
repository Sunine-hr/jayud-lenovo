package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class ViewBillForm {

    @ApiModelProperty(value = "应付出账单界面部分")
    private List<Long> costIds;


}
