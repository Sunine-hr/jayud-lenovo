package com.jayud.crm.model.form;

import com.jayud.auth.model.po.SysDictItem;
import com.jayud.crm.model.po.CrmCustomerRelations;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ciro
 * @date 2022/3/2 11:01
 * @description: 字典项
 */
@Data
@ApiModel(value="数据编码对象", description="数据编码对象")
public class CrmCodeFollowForm {

    @ApiModelProperty(value = "跟进方式")
    private List<SysDictItem> sysDictItems;

    @ApiModelProperty(value = "联系人")
    private List<CrmCustomerRelations> crmCustomerRelations;



}
