package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * 陆运费用数据明细form
 *
 * @author william
 * @description
 * @Date: 2020-09-16 18:09
 */
public class ForwarderLandTransFeeDetailForm {
    @JsonProperty("Cost_item_code")
    @SerializedName("Cost_item_code")
    @ApiModelProperty(value = "费用项目代码")
    @NotEmpty(message = "费用项目代码不为空")
    private String costItemCode;
    @JsonProperty("Currency_of_payment")
    @SerializedName("Currency_of_payment")
    @ApiModelProperty(value = "请款币别")
    @NotEmpty(message = "请款币别不为空")
    private String currencyOfPayment;
    @JsonProperty("Amount_payable")
    @SerializedName("Amount_payable")
    @ApiModelProperty(value = "请款金额")
    @NotEmpty(message = "请款金额不为空")
    private int amountPayable;
    @JsonProperty("File_size")
    @SerializedName("File_size")
    @ApiModelProperty(value = "文件大小")
    @NotEmpty(message = "文件大小不为空")
    private String fileSize;
}
