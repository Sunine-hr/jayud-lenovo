package com.jayud.airfreight.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Primary;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 货代确认订舱信息并抛转到vivo表单
 *
 * @author william
 * @description
 * @Date: 2020-09-15 11:20
 */
@Data
public class ForwarderBookingConfirmedFeedbackForm {

    @JsonProperty("Booking_no")
    @SerializedName( "Booking_no")
    @ApiModelProperty(value = "订单号")
    @NotEmpty(message = "订单号不可为空")
    @Length(max = 32, message = "订单号字段最大长度为32")
    private String bookingNo;
    @JsonProperty("Forwarder_bookingno")
    @SerializedName( "Forwarder_bookingno")
    @ApiModelProperty(value = "货代订单号")
    @Length(max = 50, message = "货代订单号字段最大长度为50")
    private String forwarderBookingno;
    @JsonProperty("Shipping_order")
    @SerializedName( "Shipping_order")
    @ApiModelProperty(value = "S/O 船单号")
    @Length(max = 50, message = "S/O 船单号字段最大长度为50")
    private String shippingOrder;
    @ApiModelProperty(value = "交货仓库代码")
    @NotNull(message = "交货仓库代码不可为空")
    @JsonProperty("Delivery_warehouse_code")
    @SerializedName( "Delivery_warehouse_code")
    private String deliveryWarehouseCode;
}
