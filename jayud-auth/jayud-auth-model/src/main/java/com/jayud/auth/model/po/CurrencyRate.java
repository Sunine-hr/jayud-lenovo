package com.jayud.auth.model.po;

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
 * CurrencyRate 实体类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="币种汇率对象", description="币种汇率")
public class CurrencyRate extends SysBaseEntity {


    @ApiModelProperty(value = "兑换币种")
    private Integer dcid;

    @ApiModelProperty(value = "原始币种")
    private Integer ocid;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "年月")
    private String month;

}
