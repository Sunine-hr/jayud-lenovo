package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ParameterToForwarderForm {

    @SerializedName("Statement_of_account")
    @JsonProperty("Statement_of_account")
    @NotEmpty(message = "Statement_of_account 必填")
    @ApiModelProperty("对账单号")
    @Length(max = 32,message = "Statement_of_account 最大长度为32位")
    private String statementOfAccount;

    @SerializedName("Settlement_currency")
    @JsonProperty("Settlement_currency")
    @NotEmpty(message = "Settlement_currency 必填")
    @ApiModelProperty("结算币别")
    @Length(max = 32,message = "Settlement_currency 最大长度为32位")
    private String settlementCurrency;

    @SerializedName("Settlement_amount")
    @JsonProperty("Settlement_amount")
    @NotEmpty(message = "Settlement_amount 必填")
    @ApiModelProperty("结算金额")
    @Length(max = 32,message = "Settlement_amount 最大长度为32位")
    private String settlementAmount;

    @SerializedName("Subject_of_contract")
    @JsonProperty("Subject_of_contract")
    @NotEmpty(message = "Subject_of_contract 必填")
    @ApiModelProperty("合同主体")
    @Length(max = 32,message = "Subject_of_contract 最大长度为32位")
    private String subjectOfContract;

    @SerializedName("Estimated_payment_time")
    @JsonProperty("Estimated_payment_time")
    @ApiModelProperty("预计付款时间")
    private String estimatedPaymentTime;

    @SerializedName("File_size")
    @JsonProperty("File_size")
    @ApiModelProperty("文件大小")
    @NotNull
    private String fileSize;
}
