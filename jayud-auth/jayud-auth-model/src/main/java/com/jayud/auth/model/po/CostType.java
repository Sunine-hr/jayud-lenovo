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
 * CostType 实体类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="费用类别对象", description="费用类别")
public class CostType extends SysBaseEntity {


    @ApiModelProperty(value = "费用类型CODE")
    private String code;

    @ApiModelProperty(value = "费用类型名称")
    private String codeName;

    @ApiModelProperty(value = "是否代垫代收（1-是 0-否）")
    private Integer isPayCollection;

    @ApiModelProperty(value = "状态（0无效 1有效）")
    private String status;



    @ApiModelProperty(value = "描述")
    private String remarks;


}
