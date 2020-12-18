package com.jayud.airfreight.model.bo.vivo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DispatchRejectedForm {

    @JsonProperty("Dispatch_no")
    @SerializedName("Dispatch_no")
    @ApiModelProperty("派车编号")
    @NotEmpty(message = "Dispatch_no 必填")
    private String dispatchNo;

}
