package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SaveWarehouseForm {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "仓库名称")
    @NotBlank(message = "仓库名称不能为空")
    @Length(max = 20, message = "仓库名称不能超过20个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "仓库名称限制：最多20字符，包含文字、字母和数字")
    private String name;

    @ApiModelProperty(value = "仓库代码")
    @NotBlank(message = "仓库代码不能为空")
    @Length(max = 20, message = "仓库代码不能超过20个字符")
    private String code;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

}
