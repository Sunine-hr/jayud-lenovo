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
 * CrmContractQuotationDetails 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="合同报价详情对象", description="合同报价详情")
public class CrmContractQuotationDetails extends SysBaseEntity {


    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "合同报价id")
    private Long contractQuotationId;

    @ApiModelProperty(value = "类型(1:整车 2:其他)")
    private Integer type;

    @ApiModelProperty(value = "起始地")
    private String startingPlace;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "车型(3T 5T 8T 10T 12T 20GP 40GP 45GP)")
    private String vehicleSize;

    @ApiModelProperty(value = "费用名称code")
    private String costCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "费用类别(作业环节)")
    private Long costTypeId;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "起始地id(多个逗号隔开)")
    private String startingPlaceId;

    @ApiModelProperty(value = "目的地id(多个逗号隔开)")
    private String destinationId;

    @ApiModelProperty(value = "重量计费/kg")
    private BigDecimal weightBilling;

    @ApiModelProperty(value = "件数计费/件")
    private BigDecimal numBilling;

    @ApiModelProperty(value = "板数计费/版")
    private BigDecimal plateNumBilling;

    @ApiModelProperty(value = "最低计费")
    private BigDecimal minBilling;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
