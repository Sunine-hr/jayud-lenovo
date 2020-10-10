package com.jayud.finance.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应付单实体（直接从报关模块复制）
 *
 * @author william
 * @description
 * @Date: 2020-09-19 10:17
 */
@Data
public class CustomsPayable {
    @JsonProperty("fee_cd")
    @SerializedName("fee_cd")
    @ApiModelProperty(value = "费用项代码")
    private String feeCd;

    @JsonProperty("fee_name")
    @SerializedName("fee_name")
    @ApiModelProperty(value = "费用名称")
    private String feeName;

    @JsonProperty("target_name")
    @SerializedName("target_name")
    @ApiModelProperty(value = "应付公司")
    private String targetName;

    @ApiModelProperty(value = "支付费用")
    private String cost;

    @JsonProperty("fee_item")
    @SerializedName("fee_item")
    @ApiModelProperty(value = "项号")
    private String feeItem;

    @JsonProperty("container_type_no")
    @SerializedName("container_type_no")
    @ApiModelProperty(value = "柜型")
    private String containerTypeNo;

    @JsonProperty("custom_apply_no")
    @SerializedName("custom_apply_no")
    @ApiModelProperty(value = "18位报关单号")
    private String customApplyNo;

    @JsonProperty("goods_name")
    @SerializedName("goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @JsonProperty("shipper_name")
    @SerializedName("shipper_name")
    @ApiModelProperty(value = "经营单位名称")
    private String shipperName;

    @JsonProperty("cabin_no")
    @SerializedName("cabin_no")
    @ApiModelProperty(value = "提运单号")
    private String cabinNo;

    @JsonProperty("apply_dt")
    @SerializedName("apply_dt")
    @ApiModelProperty(value = "报关日期")
    private String applyDt;

    @JsonProperty("container_no")
    @SerializedName("container_no")
    @ApiModelProperty(value = "柜号")
    private String containerNo;
}
