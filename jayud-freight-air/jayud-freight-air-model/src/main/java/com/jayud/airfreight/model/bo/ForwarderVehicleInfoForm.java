package com.jayud.airfreight.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * 貨代车辆信息form
 *
 * @author william
 * @description
 * @Date: 2020-09-15 17:18
 */
@Data
public class ForwarderVehicleInfoForm {
    @JsonProperty("Dispatch_no")
    @SerializedName("Dispatch_no")
    @Length(max =32 ,message = "派车编号字段的最大长度为32")
    @ApiModelProperty(value = "派车编号")
    @NotEmpty(message = "派车编号不能为空")
    private String dispatchNo;

    @JsonProperty("License_plate")
    @SerializedName("License_plate")
    @ApiModelProperty(value = "大陆车牌")
    @Length(max = 50,message = "大陆车牌字段的最大长度为50")
    @NotEmpty(message = "大陆车牌不能为空")
    private String licensePlate;

    @JsonProperty("Transportation_company")
    @SerializedName("Transportation_company")
    @ApiModelProperty(value = "运输公司")
    @Length(max = 250,message = "运输公司字段的最大长度为250")
    @NotEmpty(message = "运输公司不能为空")
    private String transportationCompany;

    @JsonProperty("Container_no")
    @SerializedName("Container_no")
    @ApiModelProperty(value = "柜号")
    @Length(max = 20,message = "柜号字段的最大长度为20")
    private String containerNo;

    @JsonProperty("Shipping_order")
    @SerializedName("Shipping_order")
    @Length(max = 50,message = "S/O号字段的最大长度为50")
    @ApiModelProperty(value = "S/O号")
    private String shippingOrder;

    @JsonProperty("Manifest_no")
    @SerializedName("Manifest_no")
    @Length(max = 20,message = "载货清单字段的最大长度为20")
    @ApiModelProperty(value = "载货清单")
    private String manifestNo;

    @JsonProperty("Seal_no")
    @SerializedName("Seal_no")
    @Length(max =20 ,message = "封条号字段的最大长度为20")
    @ApiModelProperty(value = "封条号")
    private String sealNo;

    @JsonProperty("Cabinet_weight")
    @SerializedName("Cabinet_weight")
//    @Length(max = 10,message = "吉柜重字段的最大长度为10")
    @ApiModelProperty(value = "吉柜重")
    private Float cabinetWeight;

}
