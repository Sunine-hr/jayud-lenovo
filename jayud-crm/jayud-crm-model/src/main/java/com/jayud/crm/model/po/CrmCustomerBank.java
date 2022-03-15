package com.jayud.crm.model.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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

/**
 * CrmCustomerBank 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_银行账户(crm_customer_bank)对象", description="基本档案_客户_银行账户(crm_customer_bank)")
public class CrmCustomerBank extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @ApiModelProperty(value = "银行名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bankName;

    @ApiModelProperty(value = "银行地址")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bankAddress;

    @ApiModelProperty(value = "银行代码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bankNum;

    @ApiModelProperty(value = "账户名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String accName;

    @ApiModelProperty(value = "银行账号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String accNumber;

    @ApiModelProperty(value = "开户行")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String bankDeposit;

    @ApiModelProperty(value = "账户币别名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String currencyName;

    @ApiModelProperty(value = "账户币别编码")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String currencyCode;

    @ApiModelProperty(value = "国际联行号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String swiftcode;

    @ApiModelProperty(value = "是否默认账户")
    private Boolean isDefault;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否默认")
    private String isDefaultText;





}
