package com.jayud.scm.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 委托单-柜号列表
 *
 * @author william
 * @description
 * @Date: 2020-09-07 18:19
 */
@Data
public class CustomsDetail {
    @ApiModelProperty(value = "序号")
    @JsonProperty("num_no")
    @SerializedName("num_no")
    private Integer numNo;

    @ApiModelProperty(value = "柜型")
    @JsonProperty("container_type_no")
    @SerializedName("container_type_no")
    //4选1： 20、40、45、53
    private String containerTypeNo;

    @ApiModelProperty(value = "柜型")
    @JsonProperty("container_type")
    @SerializedName("container_type")
    //2选1：GP、HQ
    private String containerType;

    @ApiModelProperty(value = "柜号")
    @JsonProperty("container_no")
    @SerializedName("container_no")
    private String containerNo;

    @ApiModelProperty(value = "是否拼柜")
    @JsonProperty("fee_type")
    @SerializedName("fee_type")
    //拼柜填写L、整柜填写F
    private String feeType;

    @ApiModelProperty(value = "封条号")
    @JsonProperty("seal_no")
    @SerializedName("seal_no")
    private String sealNo;

    @ApiModelProperty(value = "件数")
    @JsonProperty("piece")
    @SerializedName("piece")
    private Integer piece;

    @ApiModelProperty(value = "毛重(18,4)")
    @JsonProperty("grossweight")
    @SerializedName("grossweight")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "品名")
    @JsonProperty("goodsname")
    @SerializedName("goodsname")
    private String goodsName;

}
