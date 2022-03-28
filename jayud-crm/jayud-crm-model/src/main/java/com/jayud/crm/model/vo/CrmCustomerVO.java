package com.jayud.crm.model.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CrmCustomer 实体类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Data
public class CrmCustomerVO extends SysBaseEntity {

    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "简称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custNameAbbr;

    @ApiModelProperty(value = "业务ID")
    private Long fsalesId;

    @ApiModelProperty(value = "业务员名称")
    private String fsalesName;

    @ApiModelProperty(value = "商务ID")
    private Long followerId;

    @ApiModelProperty(value = "商务名称")
    private String followerName;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "公司编码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String custCode;

    @ApiModelProperty(value = "业务类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String businessTypes;

    @ApiModelProperty(value = "服务类型")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String serviceType;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "联系人名称")
    private String custRelationUsername;

    @ApiModelProperty(value = "联系人岗位")
    private String custRelationPostName;

    @ApiModelProperty(value = "结算方式")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String settlementMethod;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;



}
