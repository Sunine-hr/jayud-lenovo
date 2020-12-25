package com.jayud.airfreight.model.bo.vivo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单号")
    @NotEmpty(message = "订单号不可为空")
    @Length(max = 32, message = "订单号字段最大长度为32")
    private String bookingNo;

    @JsonProperty("Forwarder_bookingno")
    @SerializedName("Forwarder_bookingno")
    @ApiModelProperty(value = "货代订单号")
    @Length(max = 50, message = "货代订单号字段最大长度为50")
    private String forwarderBookingno;
    //    @JsonProperty("Shipping_order")
//    @SerializedName( "Shipping_order")
//    @ApiModelProperty(value = "S/O 船单号")
//    @Length(max = 50, message = "S/O 船单号字段最大长度为50")
//    private String shippingOrder;
    @ApiModelProperty(value = "交货仓库名")
    @NotNull(message = "交货仓库名不可为空")
    @JsonProperty("Delivery_warehouse")
    @SerializedName("Delivery_warehouse")
    private String deliveryWarehouse;

    @ApiModelProperty(value = "交货仓库地址")
    @NotNull(message = "交货仓库地址不可为空")
    @JsonProperty("Delivery_warehouse_address")
    @SerializedName("Delivery_warehouse_address")
    private String deliveryWarehouseAddress;


    public ApiResult checkParam() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<ForwarderBookingConfirmedFeedbackForm>> set = validator.validate(this);
        for (ConstraintViolation<ForwarderBookingConfirmedFeedbackForm> constraintViolation : set) {
            return ApiResult.error(400, constraintViolation.getMessage());
        }
        return null;
    }
}
