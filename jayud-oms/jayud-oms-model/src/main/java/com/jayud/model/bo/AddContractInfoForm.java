package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;


@Data
public class AddContractInfoForm  {

    @ApiModelProperty(value = "主键,修改时必传")
    private Long id;

    @ApiModelProperty(value = "客户名称",required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "合同编号",required = true)
    @NotEmpty(message = "contractNo is required")
    private String contractNo;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty(value = "业务类型",required = true)
    @NotEmpty(message = "businessType is required")
    private List<Long> businessTypes;

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalEntity is required")
    private String legalEntity;

    @ApiModelProperty(value = "合同起期",required = true)
    @NotEmpty(message = "startDate is required")
    private Date startDate;

    @ApiModelProperty(value = "合同止期",required = true)
    @NotEmpty(message = "endDate is required")
    private Date endDate;

}
