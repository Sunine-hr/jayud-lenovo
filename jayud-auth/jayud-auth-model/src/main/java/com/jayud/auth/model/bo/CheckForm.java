package com.jayud.auth.model.bo;

import com.jayud.common.entity.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class CheckForm extends BasePageForm {

    @ApiModelProperty(value = "单据编号")
    @NotBlank(message = "单据编号，不能为空")
    private String sheetCode;

    @ApiModelProperty(value = "记录id")
    @NotBlank(message = "记录id，不能为空")
    private Long recordId;



}
