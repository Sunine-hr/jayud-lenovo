package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * CrmCreditCust 实体类
 *
 * @author jayud
 * @since 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="基本档案_额度_额度授信管理(crm_credit_cust)对象", description="基本档案_额度_额度授信管理(crm_credit_cust)")
public class CrmCreditCust extends SysBaseEntity {


    @ApiModelProperty(value = "额度类型ID")
    private String creditId;

    @ApiModelProperty(value = "额度类型名称")
    private String creditName;

    @ApiModelProperty(value = "额度类型值(1:税费额度,2:期票额度,3:贷款额度)")
    private String creditValue;

    @ApiModelProperty(value = "法人主体id")
    private Long departId;

    @ApiModelProperty(value = "法人主体名称")
    private String departName;

    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "申请金额")
    private BigDecimal creditAmt;

    @ApiModelProperty(value = "开始日期")
    private LocalDate beginDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "申请日期")
    private LocalDateTime applyDate;

    @ApiModelProperty(value = "额度模式")
    private String creditType;

    @ApiModelProperty(value = "额度模式值(1:临时，2:标准)")
    private String creditTypeValue;

    @ApiModelProperty(value = "审核级别")
    @JsonProperty(value = "fLevel")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    @JsonProperty(value = "fStep")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    @JsonProperty(value = "checkStateFlag")
    private String checkStateFlag;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
