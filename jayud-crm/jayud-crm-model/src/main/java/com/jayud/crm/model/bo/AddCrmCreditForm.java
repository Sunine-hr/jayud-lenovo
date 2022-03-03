package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CrmCredit 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_额度_额度总量(crm_credit)对象", description="基本档案_额度_额度总量(crm_credit)")
public class AddCrmCreditForm extends SysBaseEntity {


    @ApiModelProperty(value = "额度类型ID")
    @NotBlank(message = "额度类型必填")
    private String creditId;

    @ApiModelProperty(value = "额度类型")
    @NotBlank(message = "额度类型必填")
    private String creditType;

    @ApiModelProperty(value = "额度类型值(1:税费额度,2:期票额度,3:贷款额度)")
    @NotBlank(message = "额度类型必填")
    private String creditValue;

    @ApiModelProperty(value = "授信总金额")
    @NotNull(message = "授信总金额必填")
    private BigDecimal creditMoney;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;





}
