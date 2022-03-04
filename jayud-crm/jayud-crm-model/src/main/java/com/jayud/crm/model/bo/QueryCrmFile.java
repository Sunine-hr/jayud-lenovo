package com.jayud.crm.model.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="附件", description="附件")
public class QueryCrmFile {

    @ApiModelProperty(value = "业务主键")
    private Long businessId;

    @ApiModelProperty(value = "附件类型")
    private String fileType;

    @ApiModelProperty(value = "文件集合")
    private List<CrmFileForm> crmFileForm;

    @ApiModelProperty(value = "备注")
    private String remark;
}
