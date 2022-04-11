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
 * VehicleSizeInfo 实体类
 *
 * @author jayud
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="车辆尺寸信息对象", description="车辆尺寸信息")
public class VehicleSizeInfo extends SysBaseEntity {


    @ApiModelProperty(value = "尺寸")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(2-柜车 1-吨车)")
    private Integer vehicleType;

}
