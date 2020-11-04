package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 供应商对应司机信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class QueryDriverInfoForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "司机姓名")
    private String name;

    @ApiModelProperty(value = "香港/大陆电话")
    private String phone;

    @ApiModelProperty(value = "车牌号")
    private String carNumber;



}
