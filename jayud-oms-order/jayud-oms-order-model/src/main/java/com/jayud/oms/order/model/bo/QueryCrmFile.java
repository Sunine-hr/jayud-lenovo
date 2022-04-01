package com.jayud.oms.order.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="附件", description="附件")
public class QueryCrmFile {


    @ApiModelProperty(value = "主键id")
    protected Long id;

    @ApiModelProperty(value = "业务主键")
    private Long businessId;

    @ApiModelProperty(value = "业务标识code")
    private String code;

    @ApiModelProperty(value = "附件标识编号")
    private String crmFileNumber;

    @ApiModelProperty(value = "附件类型")
    private String fileType;

    @ApiModelProperty(value = "文件集合")
    private List<CrmFileForm> crmFileForm;

    @ApiModelProperty(value = "备注")
    private String remark;
}
