package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.jayud.airfreight.model.enums.AirOrderTermsEnum;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.CreateUserTypeEnum;
import com.jayud.common.enums.OrderAddressEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

/**
 * vivo向3PL订舱请求入参
 *
 * @author william
 * @description
 * @Date: 2020-09-15 10:23
 */
@Data
public class BookingSpaceForm {
    @JsonProperty("Booking_no")
    @SerializedName("Booking_no")
    @ApiModelProperty(value = "订单号")
    @Length(max = 32, message = "订单号最大长度32位")
    @NotNull(message = "订单号为必填")
    private String bookingNo;

    @JsonProperty("Forwarder_bookingno")
    @SerializedName("Forwarder_bookingno")
    @ApiModelProperty(value = "货代订单号")
    private String forwarderBookingNo;

    @JsonProperty("Pick_up_date")
    @SerializedName("Pick_up_date")
    @ApiModelProperty(value = "提货时间")
    @NotEmpty(message = "提货时间为必填")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/M/dd HH:mm", timezone = "GMT+8")
    private String pickUpDate;

    @JsonProperty("Urgency")
    @SerializedName("Urgency")
    @ApiModelProperty(value = "紧急程度")
    @NotNull(message = "紧急程度为必填")
//    @Pattern(regexp = "1|2|3", message = "1=一般，2=经济，3=十分紧急")
    private Integer urgency;

    @JsonProperty("Invoice_no")
    @SerializedName("Invoice_no")
    @ApiModelProperty(value = "发票号，多个用英文逗号,隔开")
    @NotEmpty(message = "发票号必填")
    @Length(max = 100, message = "发票号长度不可超过100")
    private String invoiceNo;

    @JsonProperty("Place_of_departure")
    @SerializedName("Place_of_departure")
    @ApiModelProperty(value = "起运地")
    @NotEmpty(message = "起运地不可为空")
//    @Length(min = 5, max = 5, message = "启运地代码长度为五位代码")
    private String placeOfDeparture;

    @JsonProperty("Port_of_departure")
    @SerializedName("Port_of_departure")
    @ApiModelProperty(value = "起运港")
    @NotEmpty(message = "起运港不可为空")
//    @Length(min = 5, max = 5, message = "启运港代码长度为五位代码")
    private String portOfDeparture;

    @JsonProperty("Destination")
    @SerializedName("Destination")
    @ApiModelProperty(value = "目的地")
    @NotEmpty(message = "目的地不可为空")
//    @Length(min = 5, max = 5, message = "目的地代码长度为五位代码")
    private String destination;

    @JsonProperty("Port_of_destination")
    @SerializedName("Port_of_destination")
    @ApiModelProperty(value = "目的港")
    @NotEmpty(message = "目的港不可为空")
//    @Length(min = 5, max = 5, message = "目的港代码长度为五位代码")
    private String portOfDestination;

    @JsonProperty("Trade_terms")
    @SerializedName("Trade_terms")
    @ApiModelProperty(value = "贸易条款")
    @NotEmpty(message = "贸易条款不可为空")
    @Length(max = 32, message = "贸易条款代码长度最大为32位代码")
    private String tradeTerms;

    @JsonProperty("Shipper")
    @SerializedName("Shipper")
    @ApiModelProperty(value = "发货人")
    @NotEmpty(message = "发货人不可为空")
    @Length(max = 100, message = "发货人字段长度最大为100")
    private String shipper;

    @JsonProperty("Shipping_address")
    @SerializedName("Shipping_address")
    @ApiModelProperty(value = "发货人地址")
    @NotEmpty(message = "发货人地址不能为空")
    @Length(max = 200, message = "发货人地址字段的最大长度为200")
    private String shippingAddress;

    @JsonProperty("Consignee")
    @SerializedName("Consignee")
    @ApiModelProperty(value = "收货人")
    @NotEmpty(message = "收货人不能为空")
    @Length(max = 100, message = "收货人字段的最大长度为100")
    private String consignee;

    @JsonProperty("Consignee_address")
    @SerializedName("Consignee_address")
    @ApiModelProperty(value = "收货人地址")
    @NotEmpty(message = "收货人地址不能为空")
    @Length(max = 200, message = "收货人地址字段的最大长度为200")
    private String consigneeAddress;

    @JsonProperty("Logistics_operator")
    @SerializedName("Logistics_operator")
    @ApiModelProperty(value = "物流操作员")
    @NotEmpty(message = "物流操作员不能为空")
    @Length(max = 20, message = "物流操作员字段的最大长度为20")
    private String logisticsOperator;

    @JsonProperty("Total_volume")
    @SerializedName("Total_volume")
    @ApiModelProperty(value = "订单总体积(保留两位小数)")
    @NotNull(message = "订单总体积不能为空")
//    @Length(max = 10, message = "订单总体积字段的最大长度为10")
    private Float totalVolume;

    @JsonProperty("Total_gross_weight")
    @SerializedName("Total_gross_weight")
    @ApiModelProperty(value = "订单总毛重(保留两位小数)")
    @NotNull(message = "订单总毛重不能为空")
//    @Length(max = 10, message = "订单总毛重字段的最大长度为10")
    private Float totalGrossWeight;

    @JsonProperty("General_dimension")
    @SerializedName("General_dimension")
    @ApiModelProperty(value = "订单具体尺寸")
    @NotEmpty(message = "订单具体尺寸不能为空")
    @Length(max = 1000, message = "订单具体尺寸字段的最大长度为1000")
    private String generalDimension;

    @JsonProperty("Pallet_Number")
    @SerializedName("Pallet_Number")
    @ApiModelProperty(value = "卡板数")
    @NotNull(message = "卡板数不能为空")
//    @Length(max = 4, message = "卡板数字段的最大长度为4")
    private Integer palletNNumber;

    @JsonProperty("Pallet_Unit")
    @SerializedName("Pallet_Unit")
    @ApiModelProperty(value = "卡板单位(默认为Pallet)")
    @NotEmpty(message = "卡板单位不能为空")
    @Length(max = 10, message = "卡板单位字段的最大长度为10")
    private String palletUnit;

    @JsonProperty("Bulk_Box_Number")
    @SerializedName("Bulk_Box_Number")
    @ApiModelProperty(value = "散箱数")
    @NotNull(message = "散箱数不能为空")
//    @Length(max = 4, message = "散箱数字段的最大长度为4")
    private Integer bulkBoxNumber;

    @JsonProperty("Bulk_Box_Unit")
    @SerializedName("Bulk_Box_Unit")
    @ApiModelProperty(value = "散箱单位(默认为Carton)")
    @NotEmpty(message = "散箱单位不能为空")
    @Length(max = 10, message = "散箱单位字段的最大长度为10")
    private String bulkBoxUnit;

    @JsonProperty("Pick_up_warehouse")
    @SerializedName("Pick_up_warehouse")
    @ApiModelProperty(value = "提货仓库")
    @NotEmpty(message = "提货仓库不能为空")
    @Length(max = 50, message = "提货仓库字段的最大长度为50")
    private String pickUpWarehouse;

    @JsonProperty("Delivery_address")
    @SerializedName("Delivery_address")
    @ApiModelProperty(value = "提货地址")
    @NotEmpty(message = "提货地址不能为空")
    @Length(max = 200, message = "提货地址字段的最大长度为200")
    private String deliveryAddress;

    @JsonProperty("Pick_up_contact")
    @SerializedName("Pick_up_contact")
    @ApiModelProperty(value = "提货联系人")
    @NotEmpty(message = "提货联系人不能为空")
    @Length(max = 50, message = "提货联系人字段的最大长度为50")
    private String pickUpContact;

    @JsonProperty("Pick_up_contact_phone")
    @SerializedName("Pick_up_contact_phone")
    @ApiModelProperty(value = "提货联系人电话")
    @NotEmpty(message = "提货联系人电话不能为空")
    @Length(max = 50, message = "提货联系人电话字段的最大长度为50")
    private String pickUpContactPhone;

    @JsonProperty("Cargo_type")
    @SerializedName("Cargo_type")
    @ApiModelProperty(value = "货物类型")
    @NotNull(message = "货物类型不能为空")
//    @Length(max = 2, message = "货物类型字段的最大长度为2")
//    @Pattern(regexp = "1|2|3|4|5", message = "1=普货、2=电池、3=化学品、4=危险品、5=临时业务（针对一次性报价的货物都选这个类型）")
    private Integer cargoType;

    @JsonProperty("Goods")
    @SerializedName("Goods")
    @ApiModelProperty(value = "品名(多个品名用英文逗号,隔开)")
    @NotEmpty(message = "品名不能为空")
    @Length(max = 500, message = "品名字段的最大长度为500")
    private String goods;

    @JsonProperty("Mode_of_transport")
    @SerializedName("Mode_of_transport")
    @ApiModelProperty(value = "运输方式")
    @NotNull(message = "运输方式不能为空")
//    @Length(max = 1, message = "运输方式字段的最大长度为1")
//    @Pattern(regexp = "1|2|3|4", message = "1=空运、2=海运、3=铁运、4=陆运")
    private Integer modeOfTransport;


    public AddAirOrderForm convertAddAirOrderForm() {
        AddAirOrderForm addAirOrderForm = new AddAirOrderForm();
        addAirOrderForm.setThirdPartyOrderNo(this.bookingNo);
        addAirOrderForm.setPortDepartureCode(this.portOfDeparture.substring(2, 5));
        addAirOrderForm.setPortDestinationCode(this.portOfDestination.substring(2, 5));
        addAirOrderForm.setTerms(AirOrderTermsEnum.getCode(this.tradeTerms));
        addAirOrderForm.setCreateUserType(CreateUserTypeEnum.VIVO.getCode());
        addAirOrderForm.setGoodTime(this.pickUpDate);
        //发货地址信息
        AddOrderAddressForm shippingAddress = new AddOrderAddressForm();
        shippingAddress.setCompany(this.shipper);
        shippingAddress.setAddress(this.shippingAddress);
        shippingAddress.setType(OrderAddressEnum.DELIVER_GOODS.getCode());
        //收货地址信息
        AddOrderAddressForm consignee = new AddOrderAddressForm();
        consignee.setCompany(this.consignee);
        consignee.setAddress(this.consigneeAddress);
        consignee.setType(OrderAddressEnum.RECEIVING_GOODS.getCode());
        //货品信息
        AddGoodsForm addGoodsForm = new AddGoodsForm();
        addGoodsForm.setVolume(Double.valueOf(this.totalVolume));
        addGoodsForm.setTotalWeight(Double.valueOf(this.totalGrossWeight));
        addGoodsForm.setSize(this.generalDimension);
        addGoodsForm.setPlateAmount(this.palletNNumber);
        addGoodsForm.setPlateUnit(this.palletUnit);
        addGoodsForm.setBulkCargoAmount(this.bulkBoxNumber);
        addGoodsForm.setBulkCargoUnit(this.bulkBoxUnit);
        addGoodsForm.setName(this.goods);

        addAirOrderForm.setOrderAddressForms(Arrays.asList(shippingAddress, consignee));
        addAirOrderForm.setGoodsForms(Collections.singletonList(addGoodsForm));
        return addAirOrderForm;
    }

    public String checkTermsType() {
        String message = "贸易类型暂时不支持";
        for (AirOrderTermsEnum termsEnum : AirOrderTermsEnum.values()) {
            if (this.tradeTerms.contains(termsEnum.getDesc())) {
                message = "";
                break;
            }
        }
        return message;
    }

    public static void main(String[] args) {
        String s = "HHHKG";
        System.out.println(s.substring(2, 5));
    }


}
