package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 车型尺寸
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Data
public class InitVehicleSizeInfoVO {


    @ApiModelProperty(value = "柜车尺寸集合")
    private List<VehicleSizeInfoVO> cabinetCars = new ArrayList<>();

    @ApiModelProperty(value = "吨车尺寸集合")
    private List<VehicleSizeInfoVO> tonCars = new ArrayList<>();

}
