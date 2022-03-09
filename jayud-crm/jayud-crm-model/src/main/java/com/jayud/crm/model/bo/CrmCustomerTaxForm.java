package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CrmCustomerTax 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="开票资料对象", description="开票资料")
public class CrmCustomerTaxForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "税号")
    private String taxNo;

    @ApiModelProperty(value = "名称")
    private String custName;

    @ApiModelProperty(value = "地址电话")
    private String taxAddressTel;

    @ApiModelProperty(value = "银行及账号")
    private String bankAccount;

    @ApiModelProperty(value = "是否生效账户 ：0 生效 1 不生效")
    private Boolean isDefault;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
//    @TableLogic
    private Boolean isDeleted;





}
