package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商对应车辆信息-车牌信息
 * </p>
 *
 * @author cyc
 * @since 2021-10-20
 */
@Data
public class InitVehicleInfoVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

}
