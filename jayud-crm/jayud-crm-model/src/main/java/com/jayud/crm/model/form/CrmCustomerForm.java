package com.jayud.crm.model.form;

import com.jayud.crm.model.po.CrmCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/2 9:16
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_基本信息", description="基本档案_客户_基本信息")
public class CrmCustomerForm  extends CrmCustomer{

    @ApiModelProperty(value = "联系人id")
    private String custRelationId;

    @ApiModelProperty(value = "联系人名称")
    private String custRelationUsername;

    @ApiModelProperty(value = "联系人电话")
    private String custRelationPhone;

    @ApiModelProperty(value = "联系人岗位")
    private String custRelationPostName;

    @ApiModelProperty(value = "业务类型集合")
    private List<String> businessTypesList;
}
