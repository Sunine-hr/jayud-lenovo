package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 仓库信息表
 * </p>
 *
 * @author 
 * @since 2020-11-05
 */
@Data
public class QueryWarehouseInfoForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中转仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty("状态 0 禁用 1启用")
    private String status;

    @ApiModelProperty("操作指令 cmd=audit审核 list查询")
    private String cmd;
}
