package com.jayud.customs.model.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 委托单-报关单表头
 * @author william
 * @description
 * @Date: 2020-09-07 17:50
 */
@Data
public class CustomsHead {
    //后台生成的委托ID	新增时不用赋值，修改时需要赋值。新增后会将该字段返回给前台，在修改委托单时需要该值
    @ApiModelProperty(value = "后台生成的委托ID")
    private String uid;

    @ApiModelProperty(value = "报关类型")
    @JsonProperty("declare_id")
    @SerializedName("declare_id")
    //报关类型	保存委托单时需要提供，1 出口、2进口
    private Integer declareId;

    @ApiModelProperty(value = "进出口口岸编号")
    @JsonProperty("port_no2")
    @SerializedName("port_no2")
    private String portNo2;

    @ApiModelProperty(value = "申报地海关")
    @JsonProperty("port_no")
    @SerializedName("port_no")
    private String portNo;

    @ApiModelProperty(value = "进出口日期 进口必须填写, 格式YYYY-MM-DD")
    @JsonProperty("indate_dt")
    @SerializedName("indate_dt")
    private LocalDateTime indateDt;

    @ApiModelProperty(value = "SO号码")
    @JsonProperty("so_no")
    @SerializedName("so_no")
    private String soNo;

    @ApiModelProperty(value = "报关单类型编号")
    @JsonProperty("entry_type")
    @SerializedName("entry_type")
    private String entryType;

    @ApiModelProperty(value = "备案号")
    @JsonProperty("records_no")
    @SerializedName("records_no")
    private String recordsNo;

    @ApiModelProperty(value = "运输方式编号")
    @JsonProperty("transmode_no")
    @SerializedName("transmode_no")
    private String transmodeNo;

    @ApiModelProperty(value = "收发货人编号")
    @JsonProperty("shipper_no")
    @SerializedName("shipper_no")
    private String shipperNo;

    @ApiModelProperty(value = "收发货人18位信用代码")
    @JsonProperty("shipper_code")
    @SerializedName("shipper_code")
    private String shipperCode;

    @ApiModelProperty(value = "收发货人名称")
    @JsonProperty("shipper_name")
    @SerializedName("shipper_name")
    private String shipperName;

    @ApiModelProperty(value = "生产销售单位编号")
    @JsonProperty("consignee_no")
    @SerializedName("consignee_no")
    //生产销售单位编号 出口填写生产销售单位编号，进口填写消费使用单位编号
    private String consigneeNo;

    @ApiModelProperty(value = "生产销售单位18位信用代码")
    @JsonProperty("consignee_code")
    @SerializedName("consignee_code")
    //生产销售单位18位信用代码 出口填写生产销售单位18位信用代码，进口填写消费使用单位18位信用代码
    private String consigneeCode;

    @ApiModelProperty(value = "生产销售单位名称")
    @JsonProperty("consignee_name")
    @SerializedName("consignee_name")
    //生产销售单位名称 出口填写生产销售单位名称，进口填写消费使用单位名称
    private String consigneeName;

    @ApiModelProperty(value = "申报单位编号")
    @JsonProperty("agent_no")
    @SerializedName("agent_no")
    private String agentNo;

    @ApiModelProperty(value = "申报单位18位信用代码")
    @JsonProperty("agent_code")
    @SerializedName("agent_code")
    private String agentCode;

    @ApiModelProperty(value = "申报单位名称")
    @JsonProperty("agent_name")
    @SerializedName("agent_name")
    private String agentName;

    @ApiModelProperty(value = "运输工具名称")
    @JsonProperty("transname")
    @SerializedName("transname")
    private String transName;

    @ApiModelProperty(value = "监管方式编号")
    @JsonProperty("trade_no")
    @SerializedName("trade_no")
    private String tradeNo;

    @ApiModelProperty(value = "征免性质编号")
    @JsonProperty("imposemode_no")
    @SerializedName("imposemode_no")
    private String imposemodeNo;

    @ApiModelProperty(value = "结汇方式编号")
    @JsonProperty("remitmode_no")
    @SerializedName("remitmode_no")
    private String remitmodeNno;

    @ApiModelProperty(value = "许可证号")
    @JsonProperty("licence_no")
    @SerializedName("licence_no")
    private String licenceNo;

    @ApiModelProperty(value = "运抵国编号")
    @JsonProperty("start_country_no")
    @SerializedName("start_country_no")
    private String startCountryNo;

    @ApiModelProperty(value = "指运港编号")
    @JsonProperty("loadport_no")
    @SerializedName("loadport_no")
    private String loadportNo;

    @ApiModelProperty(value = "进内货源地编号")
    @JsonProperty("end_country_no")
    @SerializedName("end_country_no")
    private String endCountryNo;

    @ApiModelProperty(value = "贸易国别编号")
    @JsonProperty("trade_country_no")
    @SerializedName("trade_country_no")
    private String tradeCountryNo;

    @ApiModelProperty(value = "成交方式编号")
    @JsonProperty("bargainmode_no")
    @SerializedName("bargainmode_no")
    private String bargainModeNo;

    @ApiModelProperty(value = "运费标识")
    @JsonProperty("freightmode_no")
    @SerializedName("freightmode_no")
    private String freightModeNo;

    @ApiModelProperty(value = "运费币种编号")
    @JsonProperty("freight_currency_no")
    @SerializedName("freight_currency_no")
    private String freightCurrencyNo;

    @ApiModelProperty(value = "运费(18,4)")
    private BigDecimal freight;

    @ApiModelProperty(value = "保费标识")
    @JsonProperty("subscribemode_no")
    @SerializedName("subscribemode_no")
    private String subscribeModeNo;

    @ApiModelProperty(value = "保费币种编号")
    @JsonProperty("subscribe_currency_no")
    @SerializedName("subscribe_currency_no")
    private String subscribeCurrencyNo;

    @ApiModelProperty(value = "保费(18,4)")
    private BigDecimal subscribe;

    @ApiModelProperty(value = "杂费标识")
    @JsonProperty("incidentalmode_no")
    @SerializedName("incidentalmode_no")
    private String incidentalModeNo;

    @ApiModelProperty(value = "杂费币种编号")
    @JsonProperty("incidental_currency_no")
    @SerializedName("incidental_currency_no")
    private String incidentalCurrencyNo;

    @ApiModelProperty(value = "杂费(18,4)")
    private BigDecimal incidental;

    @ApiModelProperty(value = "合同协议号")
    @JsonProperty("contract_no")
    @SerializedName("contract_no")
    private String contractNo;

    @ApiModelProperty(value = "件数")
    private Integer piece;

    @ApiModelProperty(value = "包装方式编号")
    @JsonProperty("pack_no")
    @SerializedName("pack_no")
    private String packNo;

    @ApiModelProperty(value = "毛重(18,4)")
    @JsonProperty("grossweight")
    @SerializedName("grossweight")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重(18,4)")
    @JsonProperty("netweight")
    @SerializedName("netweight")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "车牌号")
    @JsonProperty("truck_no")
    @SerializedName("truck_no")
    private String truckNo;

    @ApiModelProperty(value = "订舱号")
    @JsonProperty("book_no")
    @SerializedName("book_no")
    private String bookNo;

    @ApiModelProperty(value = "提运单号")
    @JsonProperty("cabin_no")
    @SerializedName("cabin_no")
    private String cabinNo;

    @ApiModelProperty(value = "是否退税")
    @JsonProperty("drawback_no")
    @SerializedName("drawback_no")
    //01退税,02不退税,没有则不填写
    private String drawbackNo;

    @ApiModelProperty(value = "船名")
    private String vessel;

    @ApiModelProperty(value = "航次")
    private String voyage;

    @ApiModelProperty(value = "特殊关系确认(0或者1)")
    private String tsgxqr;

    @ApiModelProperty(value = "价格影响确认(0或者1)")
    private String jgyxqr;

    @ApiModelProperty(value ="支付特许权使用费确认(0或者1)")
    private String zftxqsyfqr;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "合同日期")
    @JsonProperty("contract_dt")
    @SerializedName("contract_dt")
    private LocalDateTime contractDt;

    @ApiModelProperty(value = "发票日期")
    @JsonProperty("invoice_dt")
    @SerializedName("invoice_dt")
    private LocalDateTime invoiceDt;

    @ApiModelProperty(value = "发票号")
    @JsonProperty("invoice_no")
    @SerializedName("invoice_no")
    private String invoiceNo;


    @ApiModelProperty(value = "签约地址")
    @JsonProperty("signed_at")
    @SerializedName("signed_at")
    private String signedAt;

    @ApiModelProperty(value = "卖方地址")
    @JsonProperty("seller_address")
    @SerializedName("seller_address")
    private String sellerAddress;

    @ApiModelProperty(value = "卖方电话")
    @JsonProperty("seller_tel")
    @SerializedName("seller_tel")
    private String sellerTel;

    @ApiModelProperty(value = "买方地址")
    @JsonProperty("buyers_address")
    @SerializedName("buyers_address")
    private String buyersAddress;

    @ApiModelProperty(value = "买方电话")
    @JsonProperty("buyers_tel")
    @SerializedName("buyers_tel")
    private String buyersTel;

    @ApiModelProperty(value = "委托号 新增的字段(限制30字)")
    @JsonProperty("bus_no")
    @SerializedName("bus_no")
    private String busNo;

    @ApiModelProperty(value = "新增字段 报关组ID")
    @JsonProperty("declare_group_id")
    @SerializedName("declare_group_id")
    private Integer declareGroupId;

    @ApiModelProperty(value = "离境口岸 单一窗口新增字段")
    @JsonProperty("despport")
    @SerializedName("despport")
    private String despPort;

    @ApiModelProperty(value = "货物存放地 单一窗口新增字段")
    @JsonProperty("goodsplace")
    @SerializedName("goodsplace")
    private String goodsPlace;

    @ApiModelProperty(value = "标记唛码 单一窗口新增字段")
    @JsonProperty("markno")
    @SerializedName("markno")
    private String markNo;

    @ApiModelProperty(value = "经停港 单一窗口新增字段")
    @JsonProperty("distinateport")
    @SerializedName("distinateport")
    private String distinatePort;

    @ApiModelProperty(value = "境外收发货人代码 单一窗口新增字段")
    @JsonProperty("buyer_no")
    @SerializedName("buyer_no")
    private String buyerNo;

    @ApiModelProperty(value = "境外收发货人英文名 单一窗口新增字段")
    @JsonProperty("buyer_name")
    @SerializedName("buyer_name")
    private String buyerName;

}
