package com.jayud.crm.model.bo;

import com.jayud.crm.model.po.CrmCustomerManager;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ciro
 * @date 2022/3/3 10:56
 * @description: 我司对接人
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_客户维护人", description="基本档案_客户_客户维护人")
public class CrmCustomerManagerForm extends CrmCustomerManager {




}
