package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Data
public class QueryAuditSupplierInfoForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    @ApiModelProperty("审核状态:1待财务审核，2待总经办审核")
    private String auditStatus;

    //审核表描述
    private String auditTableDesc;

}
