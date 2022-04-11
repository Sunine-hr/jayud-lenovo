package com.jayud.auth.model.po;

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
import lombok.experimental.Accessors;

/**
 * CurrencyInfo 实体类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="币种对象", description="币种")
public class CurrencyInfo extends SysBaseEntity {


    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种名称")
    private String currencyName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "他币与人民币的汇率")
    @TableField(exist = false)
    private BigDecimal exchangeRate;

}
