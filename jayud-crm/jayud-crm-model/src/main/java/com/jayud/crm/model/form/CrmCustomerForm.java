package com.jayud.crm.model.form;

import com.jayud.crm.model.po.CrmCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/2 9:16
 * @description:
 */
@Data
@ApiModel(value="基本档案_客户_基本信息", description="基本档案_客户_基本信息")
public class CrmCustomerForm extends CrmCustomer {

    @ApiModelProperty(value = "业务类型集合")
    private List<String> businessTypesList;
}
