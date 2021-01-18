package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class AuditWarehouseForm {

    @ApiModelProperty(value = "中转仓库ID",required = true)
    @NotNull(message = "id is required")
    private Long id;

    @ApiModelProperty(value = "中转仓代码")
    @NotEmpty(message = "warehouseCode is required")
    private String warehouseCode;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty("状态 2-通过 0-拒绝")
    @Pattern(regexp = "(0|2)", message = "只允许填写0 or 2")
    private String auditStatus;

    @ApiModelProperty(value = "当前登录用户,前台传",required = true)
    @NotEmpty(message = "loginUserName is required")
    private String loginUserName;
}
