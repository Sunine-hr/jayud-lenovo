package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.common.enums.ViVoCargoTypeEnum;
import com.jayud.common.vaildator.EnumAnnotation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CardInfoToForwarderForm {

    @JsonProperty("Dispatch_no")
    @SerializedName("Dispatch_no")
    @Length(max = 32,message = "Dispatch_no最大长度为32位")
    @NotEmpty(message = "Dispatch_no 必填")
    @ApiModelProperty("派车编号")
    private String dispatchNo;

    @JsonProperty("Pick_up_date")
    @SerializedName("Pick_up_date")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMddHHmm", timezone = "GMT+8")
    @ApiModelProperty("提货时间")
//    @NotEmpty(message = "Pick_up_date 必填")
    private String pickUpDate;

    @JsonProperty("Demand_truck_type")
    @SerializedName("Demand_truck_type")
    @ApiModelProperty("需求车型/柜型")
    @Length(max = 10,message = "demandTruckType最大长度为10位")
    @NotEmpty(message = "Demand_truck_type 必填")
    private String demandTruckType;

    @JsonProperty("Total_gross_weight")
    @SerializedName("Total_gross_weight")
    @ApiModelProperty("总毛重")
    @NotNull(message = "Total_gross_weight 必填")
    private Float totalGrossWeight;

    @JsonProperty("Total_volume")
    @SerializedName("Total_volume")
    @ApiModelProperty("总体积")
    @NotNull(message = "Total_volume 必填")
    private Float totalVolume;

    @JsonProperty("Total_package")
    @SerializedName("Total_package")
    @ApiModelProperty("总件数")
    @NotNull(message = "Total_package 必填")
    private Integer totalPackage;

    @JsonProperty("Export_customs_port")
    @SerializedName("Export_customs_port")
    @Length(max = 10,message = "Export_customs_port最大长度为10位")
    @ApiModelProperty("出口口岸")
    @NotEmpty(message = "Export_customs_port 必填")
    private String exportCustomsPort;

    @JsonProperty("Place_of_departure")
    @SerializedName("Place_of_departure")
    @Length(max = 10,message = "Place_of_departure最大长度为10位")
    @ApiModelProperty("起运地")
    @NotEmpty(message = "Place_of_departure 必填")
    private String PlaceOfDeparture;

    @JsonProperty("Port_of_departure")
    @SerializedName("Port_of_departure")
    @Length(max = 50,message = "Port_of_departure最大长度为50位")
    @ApiModelProperty("起运港")
    @NotEmpty(message = "Port_of_departure 必填")
    private String portOfDeparture;

    @JsonProperty("Delivery_address")
    @SerializedName("Delivery_address")
    @NotEmpty(message = "Delivery_address 必填")
    @Length(max = 200,message = "Delivery_address最大长度为200位")
    @ApiModelProperty("提货地址")
    private String deliveryAddress;

    @JsonProperty("Delivery_contact")
    @SerializedName("Delivery_contact")
    @Length(max = 10,message = "Delivery_contact最大长度为10位")
    @NotEmpty(message = "Delivery_contact 必填")
    @ApiModelProperty("提货仓联系人")
    private String deliveryContact;

    @JsonProperty("Consignee")
    @SerializedName("Consignee")
    @Length(max = 100,message = "Consignee最大长度为100位")
    @ApiModelProperty("收货公司")
    @NotEmpty(message = "Consignee 必填")
    private String consignee;

    @JsonProperty("Forwarder")
    @SerializedName("Forwarder")
    @Length(max = 50,message = "Forwarder最大长度为50位")
    @ApiModelProperty("物流供应商")
    @NotEmpty(message = "Forwarder 必填")
    private String forwarder;

    @JsonProperty("Shipping_order")
    @SerializedName("Shipping_order")
    @ApiModelProperty("S/O")
    private String shippingOrder;

    @JsonProperty("Line")
    @SerializedName("Line")
    @Valid
    private List<CarInfoToForwarderLineForm> line;

}
