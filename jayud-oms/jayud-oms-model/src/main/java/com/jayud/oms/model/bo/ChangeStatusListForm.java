package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


@Data
public class ChangeStatusListForm {

    @ApiModelProperty(value = "是否全勾 true-否 false-是", required = true)
    @NotEmpty(message = "checkAll is required")
    private Boolean checkAll;

    @ApiModelProperty(value = "主订单ID", required = true)
    @NotNull(message = "mainOrderId is required")
    private Long mainOrderId;

    @ApiModelProperty(value = "状态",required = true)
    @Pattern(regexp = "(CLOSE|STOP)", message = "只允许填写CLOSE/STOP")
    private String status;

    @ApiModelProperty(value = "更改状态所需参数集合", required = true)
    private List<ConfirmChangeStatusForm> forms = new ArrayList<>();


}
