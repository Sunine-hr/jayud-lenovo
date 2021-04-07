package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 审核供应商信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Data
public class AddAuditSupplierInfoForm {


    @ApiModelProperty(value = "主键ID")
    @NotNull(message = "id is required")
    private Long id;

//    @ApiModelProperty("审核页面:1财务审核，2总经办审核")
//    @NotEmpty(message = "auditPage is required")
//    private String auditPage;


    @ApiModelProperty(value = "审核操作(10:通过,11:审核失败)")
    @NotEmpty(message = "auditOperation is required")
    private String auditOperation;

    @ApiModelProperty(value = "审核描述")
    private String auditComment;

    @ApiModelProperty(value = "供应商代码,财务审核时必填")
    private String supplierCode;
}
