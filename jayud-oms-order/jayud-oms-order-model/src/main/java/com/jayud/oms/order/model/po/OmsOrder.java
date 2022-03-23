package com.jayud.oms.order.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
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

/**
 * OmsOrder 实体类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="订单管理——订单主表对象", description="订单管理——订单主表")
public class OmsOrder extends SysBaseEntity {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户参考号")
    private String refNo;

    @ApiModelProperty(value = "订单日期")
    private LocalDateTime orderDate;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "客户编号")
    private String custNo;

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "客户下单人")
    private String custBuyer;

    @ApiModelProperty(value = "客户邮箱")
    private String custMail;

    @ApiModelProperty(value = "接单主体")
    private String orgName;

    @ApiModelProperty(value = "接单部门")
    private String orgPart;

    @ApiModelProperty(value = "接单业务ID")
    private Long orgSalesId;

    @ApiModelProperty(value = "接单业务员名称")
    private String orgSalesName;

    @ApiModelProperty(value = "协议ID")
    private Long contractId;

    @ApiModelProperty(value = "协议编号")
    private String contractNo;

    @ApiModelProperty(value = "结算方式")
    private String accountType;

    @ApiModelProperty(value = "订单状态(用0，1，2表示状态，定义一个枚举)")
    private Integer stateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "流程实例")
    private Long flowInstanceId;

    @ApiModelProperty(value = "提交审核人")
    private String fMultiLevel0;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime fDateTime0;

    @ApiModelProperty(value = "最后审核人")
    private String fMultiLevel1;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "最后审核时间")
    private LocalDateTime fDateTime1;

    @ApiModelProperty(value = "业务要求")
    private String bizRemark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
    private Long tenantId;






    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private Boolean isDeleted;
    @ApiModelProperty(value = "删除人")
    private Long deletedUserId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteUserName;


}
