package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 线路-审核
 */
@Data
public class AuditLineForm {

    @ApiModelProperty(value = "主键ID",required = true)
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty(value = "审核状态 1-待审核 2-通过 3-终止 0-拒绝",required = true)
    @NotEmpty(message = "审核状态不能为空")
    @Pattern(regexp = "1|2|3|0",message = "auditStatus requires '1' or '2' or '3' or '0' only")
    private String auditStatus;
}
