package com.jayud.customs.model.po;

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
    //报关类型	保存委托单时需要提供，1 出口、2进口
    private Integer declare_id;

    @ApiModelProperty(value = "进出口口岸编号")
    private String port_no2;

    @ApiModelProperty(value = "申报地海关")
    private String port_no;

    @ApiModelProperty(value = "进出口日期 进口必须填写, 格式YYYY-MM-DD")
    private LocalDateTime indate_dt;

    @ApiModelProperty(value = "SO号码")
    private String so_no;

    @ApiModelProperty(value = "报关单类型编号")
    private String entry_type;

    @ApiModelProperty(value = "备案号")
    private String records_no;

    @ApiModelProperty(value = "运输方式编号")
    private String transmode_no;

    @ApiModelProperty(value = "收发货人编号")
    private String shipper_no;

    @ApiModelProperty(value = "收发货人18位信用代码")
    private String shipper_code;

    @ApiModelProperty(value = "收发货人名称")
    private String shipper_name;

    @ApiModelProperty(value = "生产销售单位编号")
    //生产销售单位编号 出口填写生产销售单位编号，进口填写消费使用单位编号
    private String consignee_no;

    @ApiModelProperty(value = "生产销售单位18位信用代码")
    //生产销售单位18位信用代码 出口填写生产销售单位18位信用代码，进口填写消费使用单位18位信用代码
    private String consignee_code;
    @ApiModelProperty(value = "生产销售单位名称")
    //生产销售单位名称 出口填写生产销售单位名称，进口填写消费使用单位名称
    private String consignee_name;

    @ApiModelProperty(value = "申报单位编号")
    private String agent_no;
    @ApiModelProperty(value = "申报单位18位信用代码")
    private String agent_code;
    @ApiModelProperty(value = "申报单位名称")
    private String agent_name;
    @ApiModelProperty(value = "运输工具名称")
    private String transname;
    @ApiModelProperty(value = "监管方式编号")
    private String trade_no;
    @ApiModelProperty(value = "征免性质编号")
    private String imposemode_no;
    @ApiModelProperty(value = "结汇方式编号")
    private String remitmode_no;
    @ApiModelProperty(value = "许可证号")
    private String licence_no;
    @ApiModelProperty(value = "运抵国编号")
    private String start_country_no;
    @ApiModelProperty(value = "指运港编号")
    private String loadport_no;
    @ApiModelProperty(value = "进内货源地编号")
    private String end_country_no;
    @ApiModelProperty(value = "贸易国别编号")
    private String trade_country_no;
    @ApiModelProperty(value = "成交方式编号")
    private String bargainmode_no;
    @ApiModelProperty(value = "运费标识")
    private String freightmode_no;
    @ApiModelProperty(value = "运费币种编号")
    private String freight_currency_no;
    @ApiModelProperty(value = "运费(18,4)")
    private BigDecimal freight;
    @ApiModelProperty(value = "保费标识")
    private String subscribemode_no;
    @ApiModelProperty(value = "保费币种编号")
    private String subscribe_currency_no;
    @ApiModelProperty(value = "保费(18,4)")
    private BigDecimal subscribe;
    @ApiModelProperty(value = "杂费标识")
    private String incidentalmode_no;


    @ApiModelProperty(value = "杂费币种编号")
    private String incidental_currency_no;

    @ApiModelProperty(value = "杂费(18,4)")
    private BigDecimal incidental;

    @ApiModelProperty(value = "合同协议号")
    private String contract_no;

    @ApiModelProperty(value = "件数")
    private Integer piece;

    @ApiModelProperty(value = "包装方式编号")
    private String pack_no;


    @ApiModelProperty(value = "毛重(18,4)")
    private BigDecimal grossweight;

    @ApiModelProperty(value = "净重(18,4)")
    private BigDecimal netweight;

    @ApiModelProperty(value = "车牌号")
    private String truck_no;

    @ApiModelProperty(value = "订舱号")
    private String book_no;

    @ApiModelProperty(value = "提运单号")
    private String cabin_no;

    @ApiModelProperty(value = "是否退税")
    //01退税,02不退税,没有则不填写
    private String drawback_no;

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
    private LocalDateTime contract_dt ;

    @ApiModelProperty(value = "发票日期")
    private LocalDateTime invoice_dt ;

    @ApiModelProperty(value = "发票号")
    private String invoice_no;


    @ApiModelProperty(value = "签约地址")
    private String signed_at    ;

    @ApiModelProperty(value = "卖方地址")
    private String seller_address;

    @ApiModelProperty(value = "卖方电话")
    private String seller_tel;

    @ApiModelProperty(value = "买方地址")
    private String buyers_address;

    @ApiModelProperty(value = "买方电话")
    private String buyers_tel;

    @ApiModelProperty(value = "委托号 新增的字段(限制30字)")

    private String bus_no;

    @ApiModelProperty(value = "新增字段 报关组ID")
    private Integer declare_group_id;

    @ApiModelProperty(value = "离境口岸 单一窗口新增字段")
    private String despport;

    @ApiModelProperty(value = "货物存放地 单一窗口新增字段")
    private String goodsplace;

    @ApiModelProperty(value = "标记唛码 单一窗口新增字段")
    private String markno;

    @ApiModelProperty(value = "经停港 单一窗口新增字段")
    private String distinateport;

    @ApiModelProperty(value = "境外收发货人代码 单一窗口新增字段")
    private String buyer_no;

    @ApiModelProperty(value = "境外收发货人英文名 单一窗口新增字段")
    private String buyer_name;

}
