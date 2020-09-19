package com.jayud.customs.model.bo;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 12:47
 */
@Data
public class GetFinanceInfoForm {
//    @ApiModelProperty(value = "应收应付类型：1-应付 2-应收")
//    private String costtype;

    @ApiModelProperty(value = "委托号")
    private String id;

    @ApiModelProperty(value = "委托单UID")
    private String trustId;

    @ApiModelProperty(value = "18位报关单号")
    @SerializedName("apply_no")
    private String applyNo;

    @ApiModelProperty(value = "统一编号")
    @SerializedName("unify_no")
    private String unifyNo;
}
