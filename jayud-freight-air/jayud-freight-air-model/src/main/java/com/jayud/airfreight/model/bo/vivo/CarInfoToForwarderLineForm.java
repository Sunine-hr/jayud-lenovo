package com.jayud.airfreight.model.bo.vivo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.enums.ViVoCargoTypeEnum;
import com.jayud.common.vaildator.EnumAnnotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CarInfoToForwarderLineForm {

    @JsonProperty("Contract_no")
    @SerializedName("Contract_no")
    @ApiModelProperty("合同号")
    @Length(max = 200,message = "Contract_no最大长度为200位")
    @NotEmpty(message = "Contract_no 必填")
    private String contractNo;

    @JsonProperty("Invoice_no")
    @SerializedName("Invoice_no")
    @ApiModelProperty("发票号")
    @Length(max = 200,message = "Invoice_no最大长度为200位")
    @NotEmpty(message = "Invoice_no 必填")
    private String invoiceNo;

    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty("Booking号")
    @NotEmpty(message = "Booking_no 必填")
    @Length(max = 200,message = "Booking_no最大长度为200位")
    private String bookingNo;

    @JsonProperty("Cargo_type")
    @SerializedName("Cargo_type")
    @ApiModelProperty("货物类型")
    @NotEmpty(message = "Cargo_type号 必填")
    @Length(max = 200,message = "Cargo_type最大长度为200位")
    @EnumAnnotation(message = "Cargo_type错误",target = {ViVoCargoTypeEnum.class})
    private String cargoType;

    @JsonProperty("Forwarder_booking_no")
    @SerializedName("Forwarder_booking_no")
    @ApiModelProperty("货代booking单号")
//    @NotEmpty(message = "Forwarder_Booking_no号 必填")
//    @Length(max = 200,message = "Forwarder_Booking_no最大长度为200位")
    private String forwarderBookingNo;

    @JsonProperty("Pallet_qty")
    @SerializedName("Pallet_qty")
    @ApiModelProperty("板数")
    @NotNull(message = "Pallet_qty号 必填")
    private Integer palletQty;

    @JsonProperty("Carton_qty")
    @SerializedName("Carton_qty")
    @ApiModelProperty("箱数")
    @NotNull(message = "Carton_qty号 必填")
    private Integer cartonQty;

    @JsonProperty("L_carton_qty")
    @SerializedName("L_carton_qty")
    @ApiModelProperty("散箱数")
    @NotNull(message = "L_carton_qty号 必填")
    private Integer lCartonQty;

    @JsonProperty("Package_qty")
    @SerializedName("Package_qty")
    @ApiModelProperty("件数")
    @NotNull(message = "Package_qty号 必填")
    private Integer packageQty;

    @JsonProperty("Gross_weight")
    @SerializedName("Gross_weight")
    @ApiModelProperty("毛重")
    @NotNull(message = "Gross_weight号 必填")
    private BigDecimal grossWeight;

    @JsonProperty("Volume")
    @SerializedName("Volume")
    @ApiModelProperty("体积")
    @NotNull(message = "Volume号 必填")
    private BigDecimal volume;

    @JsonProperty("Delivery_warehouse_code")
    @SerializedName("Delivery_warehouse_code")
    @ApiModelProperty("体积")
    @NotNull(message = "Delivery_warehouse_code号 必填")
    private Integer deliveryWarehouseCode;
}
