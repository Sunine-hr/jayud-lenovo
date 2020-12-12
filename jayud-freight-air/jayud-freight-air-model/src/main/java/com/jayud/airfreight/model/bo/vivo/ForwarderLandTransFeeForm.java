package com.jayud.airfreight.model.bo.vivo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.vaildator.ValidList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 陆运费用数据form
 *
 * @author william
 * @description
 * @Date: 2020-09-16 18:03
 */
@Data
public class ForwarderLandTransFeeForm {
    @JsonProperty("Dispatch_no")
    @SerializedName("Dispatch_no")
    @ApiModelProperty(value = "派车单号")
    @Length(max = 32, message = "派车单号字段长度最大为32")
    private String dispatchNo;

    @JsonProperty("Operation_type")
    @SerializedName("Operation_type")
    @ApiModelProperty(value = "操作类型")
    @Length(max = 10, message = "操作类型字段长度最大为10")
    @Pattern(regexp = "(add|update|delete)", message = "只允许填写 add/update/delete")
    private String operationType;

    @JsonProperty("Line")
    @SerializedName("Line")
    @ApiModelProperty(value = "费用明细")
    @NotEmpty(message = "费用明细不为空")
    private ValidList<ForwarderLandTransFeeDetailForm> line;
}
