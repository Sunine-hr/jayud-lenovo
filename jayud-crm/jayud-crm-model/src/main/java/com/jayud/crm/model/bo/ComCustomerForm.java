package com.jayud.crm.model.bo;

import com.jayud.auth.model.po.SysUser;
import com.jayud.crm.model.po.CrmCustomer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/4 15:06
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "公共类-客户信息", description = "公共类-客户信息")
public class ComCustomerForm {

    @ApiModelProperty(value = "客户信息集合")
    List<CrmCustomer> crmCustomerList;

    @ApiModelProperty(value = "负责人id")
    Long changerUserId;

    @ApiModelProperty(value = "负责人名称")
    String changeUserName;

    @ApiModelProperty(value = "客户id")
    Long custId;

    @ApiModelProperty(value = "是否转移,转移为true，分配为false")
    Boolean isTranfer;

    @ApiModelProperty(value = "修改对象")
    List<CrmCustomer>  changeList;

    @ApiModelProperty(value = "错误对象")
    List<CrmCustomer> errList;

    @ApiModelProperty(value = "黑名单对象")
    List<CrmCustomer> riskList;

}
