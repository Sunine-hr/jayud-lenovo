package com.jayud.finance.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 云报关应收单实体（直接从报关模块复制）
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

    @JsonProperty("_note")
    @SerializedName("_note")
    @ApiModelProperty(value = "费用备注")
    private String Note;

    @JsonProperty("BHF")
    @SerializedName("BHF")
    @ApiModelProperty(value = "编号费")
    private String bhf;

    @JsonProperty("WF")
    @SerializedName("WF")
    @ApiModelProperty(value = "网付代理费")
    private String wf;

    @JsonProperty("CGF")
    @SerializedName("CGF")
    @ApiModelProperty(value = "查柜费")
    private String cgf;

    @JsonProperty("DLF")
    @SerializedName("DLF")
    @ApiModelProperty(value = "代理费")
    private String dlf;

    @JsonProperty("GJF")
    @SerializedName("GJF")
    @ApiModelProperty(value = "国检费")
    private String gjf;

    @JsonProperty("HDF")
    @SerializedName("HDF")
    @ApiModelProperty(value = "换单费")
    private String hdf;

    @JsonProperty("KSSMF")
    @SerializedName("KSSMF")
    @ApiModelProperty(value = "快速扫描费")
    private String kssmf;

    @JsonProperty("DZF")
    @SerializedName("DZF")
    @ApiModelProperty(value = "单证费")
    private String dzf;

    @JsonProperty("DDF")
    @SerializedName("DDF")
    @ApiModelProperty(value = "打单费")
    private String ddf;

    @JsonProperty("XGFY")
    @SerializedName("XGFY")
    @ApiModelProperty(value = "修柜费用")
    private String xgfy;

    @JsonProperty("JKHDF")
    @SerializedName("JKHDF")
    @ApiModelProperty(value = "进口换单费")
    private String jkhdf;

    @JsonProperty("BGF")
    @SerializedName("BGF")
    @ApiModelProperty(value = "报关费")
    private String bgf;

    @JsonProperty("HDDLF")
    @SerializedName("HDDLF")
    @ApiModelProperty(value = "换单代理费")
    private String hddlf;

    @JsonProperty("GCF")
    @SerializedName("GCF")
    @ApiModelProperty(value = "改船费")
    private String gcf;

    @JsonProperty("BCLHF")
    @SerializedName("BCLHF")
    @ApiModelProperty(value = "驳船理货费")
    private String bclhf;

    @JsonProperty("HZQD")
    @SerializedName("HZQD")
    @ApiModelProperty(value = "保税核注清单")
    private String hzqd;

    @JsonProperty("GS")
    @SerializedName("GS")
    @ApiModelProperty(value = "关税")
    private String gs;

    @JsonProperty("ZZS")
    @SerializedName("ZZS")
    @ApiModelProperty(value = "增值税")
    private String zzs;

    @JsonProperty("YSF")
    @SerializedName("YSF")
    @ApiModelProperty(value = "运输费")
    private String ysf;

    @JsonProperty("SDCB")
    @SerializedName("SDCB")
    @ApiModelProperty(value = "删单费")
    private String sdcb;

    @JsonProperty("GBF001")
    @SerializedName("GBF001")
    @ApiModelProperty(value = "过磅费")
    private String gbf001;

    @JsonProperty("Y3C")
    @SerializedName("Y3C")
    @ApiModelProperty(value = "3C认证费用")
    private String y3c;

    @JsonProperty("XGF")
    @SerializedName("XGF")
    @ApiModelProperty(value = "续柜费")
    private String xgf;

    @JsonProperty("GBF1")
    @SerializedName("GBF1")
    @ApiModelProperty(value = "港保费")
    private String gbf1;

    @JsonProperty("SF")
    @SerializedName("SF")
    @ApiModelProperty(value = "税费")
    private String sf;

    @JsonProperty("CDF")
    @SerializedName("CDF")
    @ApiModelProperty(value = "舱单费")
    private String cdf;

    @JsonProperty("GDF")
    @SerializedName("GDF")
    @ApiModelProperty(value = "改单费")
    private String gdf;

//    @JsonProperty("tcf_")
//    @SerializedName("tcf_")
//    @ApiModelProperty(value = "舱单费")
//    private String tcf;

    @JsonProperty("CYDL")
    @SerializedName("CYDL")
    @ApiModelProperty(value = "查验代理费")
    private String cydl;

    @JsonProperty("DL")
    @SerializedName("DL")
    @ApiModelProperty(value = "代理费")
    private String dl;

    @JsonProperty("XFS")
    @SerializedName("XFS")
    @ApiModelProperty(value = "消费税")
    private String xfs;

    @JsonProperty("HWTY")
    @SerializedName("HWTY")
    @ApiModelProperty(value = "海关退运")
    private String hwty;

    @JsonProperty("FDLHF")
    @SerializedName("FDLHF")
    @ApiModelProperty(value = "分单理货费")
    private String fdlhf;

    @JsonProperty("XYF")
    @SerializedName("XYF")
    @ApiModelProperty(value = "续页费")
    private String xyf;

    @JsonProperty("GLCD")
    @SerializedName("GLCD")
    @ApiModelProperty(value = "公路舱单费")
    private String glcd;

    @JsonProperty("BJ")
    @SerializedName("BJ")
    @ApiModelProperty(value = "代理报检费")
    private String bj;

    @JsonProperty("MTF")
    @SerializedName("MTF")
    @ApiModelProperty(value = "港建费")
    private String mtf;

    @JsonProperty("MTBA")
    @SerializedName("MTBA")
    @ApiModelProperty(value = "保安费")
    private String mtba;

    @JsonProperty("DEL")
    @SerializedName("DEL")
    @ApiModelProperty(value = "删单费")
    private String del;

    @JsonProperty("OTH")
    @SerializedName("OTH")
    @ApiModelProperty(value = "其他收入")
    private String oth;

    @JsonProperty("CZF")
    @SerializedName("CZF")
    @ApiModelProperty(value = "操作费")
    private String czf;

    @JsonProperty("TTSYF")
    @SerializedName("TTSYF")
    @ApiModelProperty(value = "抬头费")
    private String ttsyf;

    @JsonProperty("BYJ")
    @SerializedName("BYJ")
    @ApiModelProperty(value = "备用金")
    private String byj;

    @JsonProperty("HGYGF")
    @SerializedName("HGYGF")
    @ApiModelProperty(value = "验估费")
    private String hgygf;

    @JsonProperty("GBF")
    @SerializedName("GBF")
    @ApiModelProperty(value = "港保费")
    private String gbf;

//    @JsonProperty("qts_")
//    @SerializedName("qts_")
//    @ApiModelProperty(value = "其他收入")
//    private String qts;

    @JsonProperty("sjbj_")
    @SerializedName("sjbj_")
    @ApiModelProperty(value = "商检报检费")
    private String sjbj;

    @JsonProperty("xzxd_")
    @SerializedName("xzxd_")
    @ApiModelProperty(value = "熏蒸消毒费")
    private String xzxd;

    @JsonProperty("sjdd_")
    @SerializedName("sjdd_")
    @ApiModelProperty(value = "商检打单费")
    private String sjdd;

    @JsonProperty("kd_")
    @SerializedName("kd_")
    @ApiModelProperty(value = "快递费")
    private String kd;

    @JsonProperty("qtc_")
    @SerializedName("qtc_")
    @ApiModelProperty(value = "其他代垫费")
    private String qtc;
}
