package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.experimental.Accessors;

/**
 * CrmCreditDepart 实体类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "基本档案_额度_部门额度授信管理(crm_credit_depart)对象", description = "基本档案_额度_部门额度授信管理(crm_credit_depart)")
public class CrmCreditDepart extends SysBaseEntity {


    @ApiModelProperty(value = "额度类型ID")
    private String creditId;

    @ApiModelProperty(value = "额度类型名称")
    private String creditName;

    @ApiModelProperty(value = "法人主体id")

    private Long departId;

    @ApiModelProperty(value = "法人主体名称")
    private String departName;

    @ApiModelProperty(value = "额度类型值(1:税费额度,2:期票额度,3:贷款额度)")
    private String creditValue;

    @ApiModelProperty(value = "授信金额")
    private BigDecimal creditAmt;

    @ApiModelProperty(value = "已授信额度")
    private BigDecimal creditGrantedMoney;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


}
