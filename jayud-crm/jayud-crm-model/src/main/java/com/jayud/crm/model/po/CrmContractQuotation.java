package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.experimental.Accessors;

/**
 * CrmContractQuotation 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "合同报价对象", description = "合同报价")
public class CrmContractQuotation extends SysBaseEntity {


    @ApiModelProperty(value = "报价编号(合同报价编号)")
    private String number;

    @ApiModelProperty(value = "客户/供应商id")
    private String customerId;

    @ApiModelProperty(value = "客户/供应商code")
    private String customerCode;

    @ApiModelProperty(value = "客户/供应商名称")
    private String customerName;

    @ApiModelProperty(value = "报价名称")
    private String name;

    @ApiModelProperty(value = "有效起始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "有效结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "合同对象(1:客户,2:供应商)")
    private Integer type;

    @ApiModelProperty(value = "法人主体名称id")
    private Long legalEntityId;

    @ApiModelProperty(value = "法人主体")
    private String legalEntity;

    @ApiModelProperty(value = "销售员id")
    private Long userId;

    @ApiModelProperty(value = "销售员名称")
    private String user;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


}
