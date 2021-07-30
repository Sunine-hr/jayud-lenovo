package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 新增部门
 * @author chuanmei
 */
@Data
public class AddCompanyForm {

    @ApiModelProperty(value = "主体简称", required = true)
    @NotEmpty(message = "主体简称不能为空")
    private String name;

    @ApiModelProperty(value = "法人主体id")
    private Long legalId;

    @ApiModelProperty(value = "主体ID,修改时必传")
    private Long id;
}
