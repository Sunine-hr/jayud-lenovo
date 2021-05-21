package com.jayud.oms.model.po;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

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
    @ApiModelProperty(value = "进出口标识（1-出口，2-进口）")
    private Integer declareId;

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
    @ApiModelProperty(value = "收发货人")
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
    @ApiModelProperty(value = "监管方式（如需名称，需要查询基础数据）")
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
    @ApiModelProperty(value = "随附单证-通关单号")
    private String accompanyNo;


    @ApiModelProperty(value = "船名")
    private String vessel;

    @ApiModelProperty(value = "航次号")
    private String voyage;

    @ApiModelProperty(value = "金关清单号-核注清单号")
    private String emsno;

    @JsonProperty("num_no")
    @SerializedName("num_no")
    @ApiModelProperty(value = "集装箱序号")
    private Integer numNo;

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
    @ApiModelProperty(value = "费用备注")
    private String costNote;

    @JsonProperty("BHF")
    @SerializedName("BHF")
    @ApiModelProperty(value = "编号费")
    private BigDecimal bhfCost;

    @JsonProperty("WF")
    @SerializedName("WF")
    @ApiModelProperty(value = "网付代理费")
    private BigDecimal wfCost;

    @JsonProperty("CGF")
    @SerializedName("CGF")
    @ApiModelProperty(value = "查柜费")
    private BigDecimal cgfCost;

    @JsonProperty("DLF")
    @SerializedName("DLF")
    @ApiModelProperty(value = "代理费")
    private BigDecimal dlfCost;

    @JsonProperty("GJF")
    @SerializedName("GJF")
    @ApiModelProperty(value = "国检费")
    private BigDecimal gjfCost;

    @JsonProperty("HDF")
    @SerializedName("HDF")
    @ApiModelProperty(value = "换单费")
    private BigDecimal hdfCost;

    @JsonProperty("KSSMF")
    @SerializedName("KSSMF")
    @ApiModelProperty(value = "快速扫描费")
    private BigDecimal kssmfCost;

    @JsonProperty("DZF")
    @SerializedName("DZF")
    @ApiModelProperty(value = "单证费")
    private BigDecimal dzfCost;

    @JsonProperty("DDF")
    @SerializedName("DDF")
    @ApiModelProperty(value = "打单费")
    private BigDecimal ddfCost;

    @JsonProperty("XGFY")
    @SerializedName("XGFY")
    @ApiModelProperty(value = "修柜费用")
    private BigDecimal xgfyCost;

    @JsonProperty("JKHDF")
    @SerializedName("JKHDF")
    @ApiModelProperty(value = "进口换单费")
    private BigDecimal jkhdfCost;

    @JsonProperty("BGF")
    @SerializedName("BGF")
    @ApiModelProperty(value = "报关费")
    private BigDecimal bgfCost;

    @JsonProperty("HDDLF")
    @SerializedName("HDDLF")
    @ApiModelProperty(value = "换单代理费")
    private BigDecimal hddlfCost;

    @JsonProperty("GCF")
    @SerializedName("GCF")
    @ApiModelProperty(value = "改船费")
    private BigDecimal gcfCost;

    @JsonProperty("BCLHF")
    @SerializedName("BCLHF")
    @ApiModelProperty(value = "驳船理货费")
    private BigDecimal bclhfCost;

    @JsonProperty("HZQD")
    @SerializedName("HZQD")
    @ApiModelProperty(value = "保税核注清单")
    private BigDecimal hzqdCost;

    @JsonProperty("BSQBGF")
    @SerializedName("BSQBGF")
    @ApiModelProperty(value = "保税区报关费")
    private BigDecimal bsqbgfCode;

    @JsonProperty("GS")
    @SerializedName("GS")
    @ApiModelProperty(value = "关税")
    private BigDecimal gsCost;

    @JsonProperty("ZZS")
    @SerializedName("ZZS")
    @ApiModelProperty(value = "增值税")
    private BigDecimal zzsCost;

    @JsonProperty("YSF")
    @SerializedName("YSF")
    @ApiModelProperty(value = "运输费")
    private BigDecimal ysfCost;

    @JsonProperty("SDCB")
    @SerializedName("SDCB")
    @ApiModelProperty(value = "删单费")
    private BigDecimal sdcbCost;

    @JsonProperty("GBF001")
    @SerializedName("GBF001")
    @ApiModelProperty(value = "过磅费")
    private BigDecimal gbf001Cost;

    @JsonProperty("Y3C")
    @SerializedName("Y3C")
    @ApiModelProperty(value = "3C认证费用")
    private BigDecimal y3cCost;

    @JsonProperty("XGF")
    @SerializedName("XGF")
    @ApiModelProperty(value = "续柜费")
    private BigDecimal xgfCost;

    @JsonProperty("GBF1")
    @SerializedName("GBF1")
    @ApiModelProperty(value = "港保费")
    private BigDecimal gbf1Cost;

    @JsonProperty("SF")
    @SerializedName("SF")
    @ApiModelProperty(value = "税费")
    private BigDecimal sfCost;

    @JsonProperty("CDF")
    @SerializedName("CDF")
    @ApiModelProperty(value = "舱单费")
    private BigDecimal cdfCost;

    @JsonProperty("GDF")
    @SerializedName("GDF")
    @ApiModelProperty(value = "改单费")
    private BigDecimal gdfCost;

//    @JsonProperty("tcf_cost")
//    @SerializedName("tcf_cost")
//    @ApiModelProperty(value = "舱单费")
//    private BigDecimal tcfCost;

    @JsonProperty("CYDL")
    @SerializedName("CYDL")
    @ApiModelProperty(value = "查验代理费")
    private BigDecimal cydlCost;

    @JsonProperty("DL")
    @SerializedName("DL")
    @ApiModelProperty(value = "代理费")
    private BigDecimal dlCost;

    @JsonProperty("XFS")
    @SerializedName("XFS")
    @ApiModelProperty(value = "消费税")
    private BigDecimal xfsCost;

    @JsonProperty("HWTY")
    @SerializedName("HWTY")
    @ApiModelProperty(value = "海关退运")
    private BigDecimal hwtyCost;

    @JsonProperty("FDLHF")
    @SerializedName("FDLHF")
    @ApiModelProperty(value = "分单理货费")
    private BigDecimal fdlhfCost;

    @JsonProperty("XYF")
    @SerializedName("XYF")
    @ApiModelProperty(value = "续页费")
    private BigDecimal xyfCost;

    @JsonProperty("GLCD")
    @SerializedName("GLCD")
    @ApiModelProperty(value = "公路舱单费")
    private BigDecimal glcdCost;

    @JsonProperty("BJ")
    @SerializedName("BJ")
    @ApiModelProperty(value = "代理报检费")
    private BigDecimal bjCost;

    @JsonProperty("MTF")
    @SerializedName("MTF")
    @ApiModelProperty(value = "港建费")
    private BigDecimal mtfCost;

    @JsonProperty("MTBA")
    @SerializedName("MTBA")
    @ApiModelProperty(value = "保安费")
    private BigDecimal mtbaCost;

    @JsonProperty("DEL")
    @SerializedName("DEL")
    @ApiModelProperty(value = "删单费")
    private BigDecimal delCost;

    @JsonProperty("OTH")
    @SerializedName("OTH")
    @ApiModelProperty(value = "其他收入")
    private BigDecimal othCost;

    @JsonProperty("CZF")
    @SerializedName("CZF")
    @ApiModelProperty(value = "操作费")
    private BigDecimal czfCost;

    @JsonProperty("TTSYF")
    @SerializedName("TTSYF")
    @ApiModelProperty(value = "抬头费")
    private BigDecimal ttsyfCost;

    @JsonProperty("BYJ")
    @SerializedName("BYJ")
    @ApiModelProperty(value = "备用金")
    private BigDecimal byjCost;

    @JsonProperty("HGYGF")
    @SerializedName("HGYGF")
    @ApiModelProperty(value = "验估费")
    private BigDecimal hgygfCost;

    @JsonProperty("GBF")
    @SerializedName("GBF")
    @ApiModelProperty(value = "港保费")
    private BigDecimal gbfCost;

//    @JsonProperty("qts_cost")
//    @SerializedName("qts_cost")
//    @ApiModelProperty(value = "其他收入")
//    private BigDecimal qtsCost;

    @JsonProperty("sjbj_cost")
    @SerializedName("sjbj_cost")
    @ApiModelProperty(value = "商检报检费")
    private BigDecimal sjbjCost;

    @JsonProperty("xzxd_cost")
    @SerializedName("xzxd_cost")
    @ApiModelProperty(value = "熏蒸消毒费")
    private BigDecimal xzxdCost;

    @JsonProperty("sjdd_cost")
    @SerializedName("sjdd_cost")
    @ApiModelProperty(value = "商检打单费")
    private BigDecimal sjddCost;

    @JsonProperty("kd_cost")
    @SerializedName("kd_cost")
    @ApiModelProperty(value = "快递费")
    private BigDecimal kdCost;

    @JsonProperty("qtc_cost")
    @SerializedName("qtc_cost")
    @ApiModelProperty(value = "其他代垫费")
    private BigDecimal qtcCost;

    @ApiModelProperty("原始数据")
    //用于通过比对字段名动态获取费用信息
    private JSONObject originData;
}
