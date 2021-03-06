package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商对应车辆信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class QueryVehicleInfoForm extends BasePageForm {

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "供应商名字")
    private String supplierName;

    @ApiModelProperty("状态 0 禁用 1启用")
    private String status;

    @ApiModelProperty(value = "车辆类型(0:中港车,1:内陆车)")
    private Integer type;

}
