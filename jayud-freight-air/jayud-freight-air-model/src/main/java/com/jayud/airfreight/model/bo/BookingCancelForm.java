package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BookingCancelForm {

    @JsonProperty("Booking_no")
    @SerializedName( "Booking_no")
    @NotEmpty(message = "Booking编号必填")
    @Length(max = 32, message = "Booking编号最大长度32位")
    @ApiModelProperty(value = "Booking编号")
    private String bookingNo;

    @JsonProperty("Forwarder_bookingno")
    @SerializedName( "Forwarder_bookingno")
    @ApiModelProperty(value = "货代Booking编号")
    private String forwarderBookingNo;

    @JsonProperty("Status")
    @SerializedName( "Status")
    @ApiModelProperty(value = "状态")
    private Integer status;

}
