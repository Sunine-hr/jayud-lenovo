package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckForm extends BasePageForm {

    @ApiModelProperty(value = "单据编号")
    @NotBlank(message = "单据编号，不能为空")
    private String sheetCode;

    @ApiModelProperty(value = "记录id")
    @NotBlank(message = "记录id，不能为空")
    private Long recordId;

    @ApiModelProperty(value = "按钮code")
    @NotBlank(message = "按钮code，不能为空")
    private String menuCode;

    @ApiModelProperty(value = "审核状态  通过，不通过")
//    @NotBlank(message = "审核状态，不能为空")
    private Boolean check;

    @ApiModelProperty(value = "拒绝原因")
    private String checkRemark;

}
