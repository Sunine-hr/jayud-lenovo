package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.vaildator.DateTime;
import com.jayud.common.vaildator.NumberEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 追踪信息
 *
 * @author william
 * @description
 * @Date: 2020-09-16 17:20
 */
@Data
public class ForwarderLadingInfoForm {
    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单号")
    @NotEmpty(message = "订单号不能为空")
    @Length(max = 32, message = "订单号字段的最大长度为32")
    private String bookingNo;

    @JsonProperty("Forwarder_booking_no")
    @SerializedName("Forwarder_booking_no")
    @ApiModelProperty(value = "货代订单号")
    @Length(max = 50, message = "货代订单号字段的最大长度为50")
    private String forwarderBookingNo;

    @JsonProperty("Pick_up_date")
    @SerializedName("Pick_up_date")
    @ApiModelProperty(value = "提货时间")
    @Length(max = 20, message = "提货时间字段的最大长度为20")
    @NotEmpty(message = "提货时间不可为空")
    private String pickUpDate;

    @JsonProperty("Bill_of_lading")
    @SerializedName("Bill_of_lading")
    @ApiModelProperty(value = "提单号")
    @Length(max = 50, message = "提单号字段的最大长度为50")
    private String billOfLading;

    @JsonProperty("Master_airway_bill")
    @SerializedName("Master_airway_bill")
    @ApiModelProperty(value = "大提单号")
    @Length(max = 50, message = "大提单号字段的最大长度为50")
    private String masterAirwayBill;

    @JsonProperty("Flight_no")
    @SerializedName("航班号")
    @ApiModelProperty(value = "航班号")
    @Length(max = 10, message = "字段的最大长度为10")
    private String flightNo;

    @JsonProperty("Charged_weight")
    @SerializedName("Charged_weight")
    @ApiModelProperty(value = "计费重量")
    @Length(max = 10, message = "字段的最大长度为10")
    private Float chargedWeight;

    @JsonProperty("B/L_weight")
    @SerializedName("B/L_weight")
    @ApiModelProperty(value = "提单重量")
    @Length(max = 10, message = "提单重量字段的最大长度为10")
    private Float blWeight;

    @JsonProperty("ETD")
    @SerializedName("ETD")
    @ApiModelProperty(value = "预计起飞时间")
    @Length(max = 20, message = "预计起飞时间字段的最大长度为20")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
    private String etd;

    @JsonProperty("ATD")
    @SerializedName("ATD")
    @ApiModelProperty(value = "实际起飞时间")
    @Length(max = 20, message = "实际起飞时间字段的最大长度为20")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
    private String atd;

    @JsonProperty("ETA")
    @SerializedName("ETA")
    @ApiModelProperty(value = "预计到港时间")
    @Length(max = 20, message = "预计到港时间字段的最大长度为20")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
    private String eta;

    @JsonProperty("ATA")
    @SerializedName("ATA")
    @ApiModelProperty(value = "实际到港时间")
    @Length(max = 20, message = "实际到港时间字段的最大长度为20")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
    private String ata;

    @JsonProperty("Inbound_date")
    @SerializedName("Inbound_date")
    @ApiModelProperty(value = "入仓日期")
    @Length(max = 20, message = "入仓日期字段的最大长度为20")
    @DateTime(format = "yyyy/M/dd HH:mm", message = "异常处理完成时间必须为时间格式yyyy/M/dd HH:mm")
    private String inboundDate;

    @JsonProperty("Remarks")
    @SerializedName("Remarks")
    @ApiModelProperty(value = "备注信息")
    @Length(max = 200, message = "备注信息字段的最大长度为200")
    private String remarks;

    @JsonProperty("Mode_of_transport")
    @SerializedName("Mode_of_transport")
    @ApiModelProperty(value = "运输方式")
    @NotNull(message = "运输方式不能为空")
    @Length(max = 1, message = "运输方式字段的最大长度为1")
    @NumberEnum(enums = "1,2,3,4", message = "空运跟踪表中运输方式（空运：1；铁运：2；海运：3；陆运：4）")
    private Integer modeOfTransport;
}
