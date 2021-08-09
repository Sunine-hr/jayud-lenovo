package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户财务编号表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCustomerClassForm {

    @ApiModelProperty(value = "自动id")
      private Integer id;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "类型名称(客户，供应商，运输公司，报关公司)")
    private String className;

    @ApiModelProperty(value = "财务系统编号(用于凭证维度或核算项目)")
    private String financeNo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
