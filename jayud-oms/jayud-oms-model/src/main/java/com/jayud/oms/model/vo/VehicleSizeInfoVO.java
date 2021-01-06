package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 车型尺寸
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Data
public class VehicleSizeInfoVO {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "尺寸")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(2-柜车 1-吨车)")
    private Integer vehicleType;



}
