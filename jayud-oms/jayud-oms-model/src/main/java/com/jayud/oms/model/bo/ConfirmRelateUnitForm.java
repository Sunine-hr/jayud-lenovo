package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConfirmRelateUnitForm {

    @ApiModelProperty(value = "客户ID",required = true)
    @NotEmpty(message = "customerInfoId is required")
    private Long customerInfoId;

    @Valid
    @ApiModelProperty(value = "关联客户集合",required = true)
    private List<CustomerInfoForm> unitInfos = new ArrayList<>();

    @ApiModelProperty(value = "当前登录用户,前台传",required = true)
    @NotEmpty(message = "loginUserName is required")
    private String loginUserName;


}
