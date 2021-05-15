package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class AddContractInfoForm  {

    @ApiModelProperty(value = "主键,修改时必传")
    private Long id;

    @ApiModelProperty(value = "客户名称/供应商名称",required = true)
//    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "合同编号",required = true)
    @NotEmpty(message = "contractNo is required")
    private String contractNo;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty(value = "合同地址集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "业务类型",required = true)
    @NotEmpty(message = "businessType is required")
    private List<Long> businessTypes;

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalEntity is required")
    private String legalEntity;

    @ApiModelProperty(value = "合同起期",required = true)
    @NotEmpty(message = "startDate is required")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "合同止期",required = true)
    @NotEmpty(message = "endDate is required")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "合同类型 0:客户合同 1:供应商合同")
    @NotEmpty(message = "type is required")
    private String type;

    @ApiModelProperty(value = "合同绑定的业务id")
    @NotEmpty(message = "bindId is required")
    private Long bindId;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
