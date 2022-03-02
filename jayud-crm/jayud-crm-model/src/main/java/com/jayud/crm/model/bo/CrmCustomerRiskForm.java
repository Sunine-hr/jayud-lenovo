package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * CrmCustomerRisk 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "基本档案_客户_风险客户（crm_customer_risk）对象", description = "基本档案_客户_风险客户（crm_customer_risk）")
public class CrmCustomerRiskForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String custName;

    @NotBlank(message = "风险类型不能为空")
    @ApiModelProperty(value = "风险类型")
    private String riskType;

    @ApiModelProperty(value = "经营范围")
    private String introduction;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "统一信用代码")
    private String unCreditCode;

    @ApiModelProperty(value = "创建时间")
    private List<String> creationTime;
}
