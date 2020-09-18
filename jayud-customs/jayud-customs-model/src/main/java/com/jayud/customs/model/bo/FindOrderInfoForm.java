package com.jayud.customs.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 委托单查询form
 *
 * @author william
 * @description
 * @Date: 2020-09-08 11:01
 */
@Data
public class FindOrderInfoForm  {
    @ApiModelProperty(value = "报关行")
    @JsonProperty("bg_org_short")
    @SerializedName("bg_org_short")
    private String bgOrgShort;

    @ApiModelProperty(value = "SO号")
    @JsonProperty("so_no")
    @SerializedName("so_no")
    private String soNo;

    @ApiModelProperty(value = "货名")
    @JsonProperty("goods_name")
    @SerializedName("goods_name")
    private String goodsName;

    @ApiModelProperty(value = "柜号")
    @JsonProperty("container_no1")
    @SerializedName("container_no1")
    private String containerNo1;

    @ApiModelProperty(value = "委托单号")
    @JsonProperty("trust_no")
    @SerializedName("trust_no")
    private String trustNo;

    @ApiModelProperty(value = "开始委托日期")
    @JsonProperty("created_dt_from")
    @SerializedName("created_dt_from")
    private String createdDtFrom;

    @ApiModelProperty(value = "结束委托日期")
    @JsonProperty("created_dt_to")
    @SerializedName("created_dt_to")
    private String createdDtTo;

    @ApiModelProperty(value = "是否发送")
    @JsonProperty("sendYn")
    @SerializedName("sendYn")
    //（1已发送、0为发送、-1全部）
    private Integer sendYn;
}
