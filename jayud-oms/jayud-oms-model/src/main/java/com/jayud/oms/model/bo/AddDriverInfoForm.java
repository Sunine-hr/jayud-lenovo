package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 供应商对应司机信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class AddDriverInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "司机姓名")
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "是否主司机(0否 1是)")
    @NotEmpty(message = "isMain is required")
    private String isMain;

    @ApiModelProperty(value = "香港电话")
    private String hkPhone;

    @ApiModelProperty(value = "大陆电话")
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "车牌号id")
    @NotNull(message = "vehicleId is required")
    private Long vehicleId;

    @ApiModelProperty(value = "身份证号")
    private String idNo;

    @ApiModelProperty(value = "驾驶证")
    private String drivingNo;

    //    @ApiModelProperty(value = "密码")
//    @NotEmpty(message = "password is required")
//    public String password;

}
