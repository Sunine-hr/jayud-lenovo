package com.jayud.crm.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * CrmCredit 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="基本档案_额度_额度总量(crm_credit)对象", description="基本档案_额度_额度总量(crm_credit)")
public class CrmCreditVO extends SysBaseEntity {


    @ApiModelProperty(value = "额度类型ID")
    @JsonProperty(value = "creditId")
    private String creditId;

    @ApiModelProperty(value = "额度类型")
    @JsonProperty(value = "creditType")
    private String creditType;

    @ApiModelProperty(value = "额度类型值(1:税费额度,2:期票额度,3:贷款额度)")
    @JsonProperty(value = "creditValue")
    private String creditValue;

    @ApiModelProperty(value = "授信总金额")
    @JsonProperty(value = "creditMoney")
    private BigDecimal creditMoney;

    @ApiModelProperty(value = "已授信额度")
    @JsonProperty(value = "creditGrantedMoney")
    private BigDecimal creditGrantedMoney;

    @ApiModelProperty(value = "租户编码")
    @JsonProperty(value = "tenantCode")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    @JsonProperty(value = "remark")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    @JsonProperty(value = "isDeleted")
    private Boolean isDeleted;

    @ApiModelProperty(value = "授信率")
    @JsonProperty(value = "creditRate")
    private String creditRate;





}
