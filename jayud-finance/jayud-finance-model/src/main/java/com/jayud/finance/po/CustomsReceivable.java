package com.jayud.finance.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 云报关应收单实体
 *
 * @author william
 * @description
 * @Date: 2020-09-18 18:38
 */
@Data
public class CustomsReceivable {
    @JsonProperty("declare_id")
    @SerializedName("declare_id")
    @ApiModelProperty(value = "当前报关单序号（无用）")
    private int declareId;

    @JsonProperty("port_no")
    @SerializedName("port_no")
    @ApiModelProperty(value = "口岸编号（如需名称需要调用基础信息）")
    private String portNo;

    @JsonProperty("customer_name")
    @SerializedName("customer_name")
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @JsonProperty("shipper_name")
    @SerializedName("shipper_name")
    @ApiModelProperty(value = "经营单位")
    private String shipperName;

    @JsonProperty("cust_linkman")
    @SerializedName("cust_linkman")
    @ApiModelProperty(value = "客户联系人")
    private String custLinkman;

    @JsonProperty("custom_apply_no")
    @SerializedName("custom_apply_no")
    @ApiModelProperty(value = "海关18位报关单号")
    private String customApplyNo;

    @JsonProperty("apply_dt")
    @SerializedName("apply_dt")
    @ApiModelProperty(value = "报关日期")
    private String applyDt;

    @JsonProperty("goods_name")
    @SerializedName("goods_name")
    @ApiModelProperty(value = "货物名")
    private String goodsName;

    @JsonProperty("cabin_no")
    @SerializedName("cabin_no")
    @ApiModelProperty(value = "提运单号")
    private String cabinNo;

    @JsonProperty("contract_no")
    @SerializedName("contract_no")
    @ApiModelProperty(value = "合同号")
    private String contractNo;

    @JsonProperty("trade_no")
    @SerializedName("trade_no")
    @ApiModelProperty(value = "贸易方式编号（如需名称，需要查询基础数据）")
    private String tradeNo;

    @JsonProperty("start_country_no")
    @SerializedName("start_country_no")
    @ApiModelProperty(value = "国家")
    private String startCountryNo;

    @JsonProperty("bus_no")
    @SerializedName("bus_no")
    @ApiModelProperty(value = "客户业务号")
    private String busNo;

    @JsonProperty("book_no")
    @SerializedName("book_no")
    @ApiModelProperty(value = "订舱号")
    private String bookNo;

    @JsonProperty("accompany_no")
    @SerializedName("accompany_no")
    @ApiModelProperty(value = "随附单证编号")
    private String accompanyNo;


    @ApiModelProperty(value = "船名")
    private String vessel;

    @ApiModelProperty(value = "航次号")
    private String voyage;

    @ApiModelProperty(value = "金关清单号")
    private String emsno;

    @JsonProperty("num_no")
    @SerializedName("num_no")
    @ApiModelProperty(value = "序号")
    private int numNo;

    @JsonProperty("container_type_no")
    @SerializedName("container_type_no")
    @ApiModelProperty(value = "柜型")
    private String containerTypeNo;

    @JsonProperty("container_no")
    @SerializedName("container_no")
    @ApiModelProperty(value = "柜号")
    private String containerNo;

    @JsonProperty("cost_note")
    @SerializedName("cost_note")
    @ApiModelProperty(value = "未对应")
    private String costNote;

    @JsonProperty("apply_cost")
    @SerializedName("apply_cost")
    @ApiModelProperty(value = "")
    private int applyCost;

    @JsonProperty("wharf_cost")
    @SerializedName("wharf_cost")
    @ApiModelProperty(value = "港保费")
    private int wharfCost;

    @JsonProperty("protect_cost")
    @SerializedName("protect_cost")
    @ApiModelProperty(value = "")
    private int protectCost;

    @JsonProperty("sanitate_cost")
    @SerializedName("sanitate_cost")
    @ApiModelProperty(value = "")
    private int sanitateCost;

    @JsonProperty("hgcg_cost")
    @SerializedName("hgcg_cost")
    @ApiModelProperty(value = "")
    private int hgcgCost;

    @JsonProperty("sjcg_cost")
    @SerializedName("sjcg_cost")
    @ApiModelProperty(value = "")
    private int sjcgCost;

    @JsonProperty("fc_cost")
    @SerializedName("fc_cost")
    @ApiModelProperty(value = "")
    private int fcCost;

    @JsonProperty("bclh_cost")
    @SerializedName("bclh_cost")
    @ApiModelProperty(value = "")
    private int bclhCost;

    @JsonProperty("bgxy_cost")
    @SerializedName("bgxy_cost")
    @ApiModelProperty(value = "")
    private int bgxyCost;

    @JsonProperty("bh_cost")
    @SerializedName("bh_cost")
    @ApiModelProperty(value = "")
    private int bhCost;

    @JsonProperty("dd_cost")
    @SerializedName("dd_cost")
    @ApiModelProperty(value = "")
    private int ddCost;

    @JsonProperty("ddxy_cost")
    @SerializedName("ddxy_cost")
    @ApiModelProperty(value = "")
    private int ddxyCost;

    @JsonProperty("zml_cost")
    @SerializedName("zml_cost")
    @ApiModelProperty(value = "")
    private int zmlCost;

    @JsonProperty("gjsb_cost")
    @SerializedName("gjsb_cost")
    @ApiModelProperty(value = "")
    private int gjsbCost;

    @JsonProperty("sjdl_cost")
    @SerializedName("sjdl_cost")
    @ApiModelProperty(value = "")
    private int sjdlCost;

    @JsonProperty("sjhz_cost")
    @SerializedName("sjhz_cost")
    @ApiModelProperty(value = "")
    private int sjhzCost;

    @JsonProperty("sjsz_cost")
    @SerializedName("sjsz_cost")
    @ApiModelProperty(value = "")
    private int sjszCost;

    @JsonProperty("sjbj_cost")
    @SerializedName("sjbj_cost")
    @ApiModelProperty(value = "")
    private int sjbjCost;

    @JsonProperty("xzxd_cost")
    @SerializedName("xzxd_cost")
    @ApiModelProperty(value = "")
    private int xzxdCost;

    @JsonProperty("sjdd_cost")
    @SerializedName("sjdd_cost")
    @ApiModelProperty(value = "")
    private int sjddCost;

    @JsonProperty("jkhd_cost")
    @SerializedName("jkhd_cost")
    @ApiModelProperty(value = "")
    private int jkhdCost;

    @JsonProperty("kd_cost")
    @SerializedName("kd_cost")
    @ApiModelProperty(value = "")
    private int kdCost;

    @JsonProperty("gc_cost")
    @SerializedName("gc_cost")
    @ApiModelProperty(value = "")
    private int gcCost;

    @JsonProperty("kb_cost")
    @SerializedName("kb_cost")
    @ApiModelProperty(value = "")
    private int kbCost;

    @JsonProperty("ld_cost")
    @SerializedName("ld_cost")
    @ApiModelProperty(value = "")
    private int ldCost;

    @SerializedName("qtc_cost")
    @ApiModelProperty(value = "")
    @JsonProperty("qtc_cost")
    private int qtcCost;

    @JsonProperty("qts_cost")
    @SerializedName("qts_cost")
    @ApiModelProperty(value = "")
    private int qtsCost;

    @JsonProperty("tcf_cost")
    @SerializedName("tcf_cost")
    @ApiModelProperty(value = "")
    private int tcfCost;
}
