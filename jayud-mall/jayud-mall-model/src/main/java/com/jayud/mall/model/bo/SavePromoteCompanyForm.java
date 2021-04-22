package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SavePromoteCompanyForm {


    @ApiModelProperty(value = "公司id/渠道id")
    private Integer companyId;

    @ApiModelProperty(value = "父id(promote_company company_id)")
    private Integer parentId;

    @ApiModelProperty(value = "公司名称/渠道公司名称")
    @NotBlank(message = "公司名称/渠道公司名称不能为空")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    @NotBlank(message = "联系人不能为空")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @ApiModelProperty(value = "公司地址")
    @NotBlank(message = "联系电话不能为空")
    private String companyAddress;

    @ApiModelProperty(value = "标题")
    private String title;

}
