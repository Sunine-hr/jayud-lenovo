package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CarCancelForm {

    @JsonProperty("Dispatch_no")
    @SerializedName("Dispatch_no")
    @ApiModelProperty("派车编号")
    @NotEmpty(message = "Dispatch_no 必填")
    private String dispatchNo;


    @JsonProperty("Status")
    @SerializedName("Status")
    @ApiModelProperty("状态（保留字段）")
    private Integer status;
}
