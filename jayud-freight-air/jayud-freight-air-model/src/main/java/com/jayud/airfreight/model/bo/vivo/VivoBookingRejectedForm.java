package com.jayud.airfreight.model.bo.vivo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.airfreight.model.po.AirExtensionField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class VivoBookingRejectedForm extends Model<AirExtensionField> {
    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单号")
    @Length(max = 32, message = "订单号最大长度32位")
    @NotNull(message = "订单号为必填")
    private String bookingNo;

    @JsonProperty("Status")
    @SerializedName("Status")
    @ApiModelProperty(value = "订单号")
    @Length(max = 1, message = "订单号最大长度1位")
    @NotNull(message = "订单号为必填")
    private String status;
}
