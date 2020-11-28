package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 核销
 */
@Data
public class HeXiaoConfirmListForm {

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "当前登录用户",required = true)
    private String loginUserName;

    @Valid
    @ApiModelProperty(value = "核销集合",required = true)
    private List<HeXiaoConfirmForm> heXiaoConfirmForms = new ArrayList<>();

}
