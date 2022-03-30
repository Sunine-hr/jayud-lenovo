package com.jayud.oms.order.model.bo;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * OmsOrder 实体类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Data
public class OmsOrderForm extends SysBaseEntity {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户参考号")
    private String refNo;

    @ApiModelProperty(value = "订单日期")
    private LocalDateTime orderDate;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "业务类型")
    private List<String> bizTypeList;

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

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime ;

    @ApiModelProperty(value = "结算方式")
    private String accountType;

    @ApiModelProperty(value = "订单状态(用0，1，2表示状态，定义一个枚举)")
    private Integer stateFlag;

    @ApiModelProperty(value = "审核级别")
    @JsonProperty("fLevel")
    private Integer fLevel;

    @ApiModelProperty(value = "当前级别")
    @JsonProperty("fStep")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "流程实例")
    private Long flowInstanceId;

    @ApiModelProperty(value = "提交审核人")
    @JsonProperty("fMultiLevel0")
    private String fMultiLevel0;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "提交时间")
    @JsonProperty("fDateTime0")
    private LocalDateTime fDateTime0;

    @ApiModelProperty(value = "最后审核人")
    @JsonProperty("fMultiLevel1")
    private String fMultiLevel1;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "最后审核时间")
    @JsonProperty("fDateTime1")
    private LocalDateTime fDateTime1;

    @ApiModelProperty(value = "业务要求")
    private String bizRemark;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
    private Long tenantId;

    public String check(){
        if(StringUtils.isEmpty(custName)){
            return "客户不为空";
        }
        if(StringUtils.isEmpty(bizType)){
            return "业务类型不为空";
        }
        if(StringUtils.isEmpty(orgName)){
            return "接单主体不为空";
        }
        if(StringUtils.isEmpty(orgPart)){
            return "接单部门不为空";
        }
        if(null == operationTime){
            return "操作时间不为空";
        }
        return "pass";
    }

}
