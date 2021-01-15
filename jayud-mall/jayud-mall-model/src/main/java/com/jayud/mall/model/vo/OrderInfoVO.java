package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value="OrderInfoVO对象", description="产品订单表")
public class OrderInfoVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "客户ID(customer id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer customerId;

    @ApiModelProperty(value = "报价id(offer_info id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer offerInfoId;

    @ApiModelProperty(value = "订柜尺寸[海运费](template_cope_receivable specification_code -> quotation_type code)", position = 4)
    @JSONField(ordinal = 4)
    private String reserveSize;

    @ApiModelProperty(value = "集货仓库代码[内陆费](template_cope_receivable specification_code ->shipping_area warehouse_code)", position = 5)
    @JSONField(ordinal = 5)
    private String storeGoodsWarehouseCode;

    @ApiModelProperty(value = "集货仓库名称(shipping_area warehouse_name)", position = 6)
    @JSONField(ordinal = 6)
    private String storeGoodsWarehouseName;

    @ApiModelProperty(value = "目的仓库代码(fab_warehouse warehouse_code)", position = 7)
    @JSONField(ordinal = 7)
    private String destinationWarehouseCode;

    @ApiModelProperty(value = "目的仓库名称(fab_warehouse warehouse_name)", position = 8)
    @JSONField(ordinal = 8)
    private String destinationWarehouseName;

    @ApiModelProperty(value = "是否上门提货(0否 1是,order_pick)", position = 9)
    @JSONField(ordinal = 9)
    private Integer isPick;

    @ApiModelProperty(value = "状态码" +
            "n枚举: -1,0,10,20,30,40,50" +
            "枚举备注: " +
            "-1 已取消 查看详情 " +
            "0 草稿-----提交、取消、查看订单详情（后台不记录数据） " +
            "10 已下单：编辑、查看订单详情 " +
            "20 已收货：编辑、查看订单详情 " +
            "30 订单确认：确认计柜重（不可修改订单信息） " +
            "40 转运中：查看订单详情 50 已签收：账单确认、查看订单详情", position = 10
    )
    @JSONField(ordinal = 10)
    private Integer status;

    @ApiModelProperty(value = "状态名称", position = 11)
    @JSONField(ordinal = 11)
    private String statusName;

    @ApiModelProperty(value = "创建日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人ID(customer id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer createUserId;

    @ApiModelProperty(value = "创建人名称(customer user_name)", position = 14)
    @JSONField(ordinal = 14)
    private String createUserName;

    @ApiModelProperty(value = "订单来源，默认为1，web端；", position = 15)
    @JSONField(ordinal = 15)
    private String orderOrigin;

    @ApiModelProperty(value = "提单号，根据配载单关联规则生成", position = 16)
    @JSONField(ordinal = 16)
    private Integer bolNo;

    @ApiModelProperty(value = "是否需要报关0-否，1-是 (订单对应报关文件:order_customs_file)", position = 17)
    @JSONField(ordinal = 17)
    private Integer needDeclare;

    @ApiModelProperty(value = "是否需要清关0-否，1-是 (订单对应清关文件:order_clearance_file)", position = 18)
    @JSONField(ordinal = 18)
    private Integer needClearance;

    @ApiModelProperty(value = "备注(订单备注)", position = 19)
    @JSONField(ordinal = 19)
    private String remark;

    //订单补充计费重信息
    @ApiModelProperty(value = "收费重(KG)", position = 20)
    @JSONField(ordinal = 20)
    private BigDecimal chargeWeight;

    @ApiModelProperty(value = "材积重(KG)", position = 21)
    @JSONField(ordinal = 21)
    private BigDecimal volumeWeight;

    @ApiModelProperty(value = "实际重量(KG)", position = 22)
    @JSONField(ordinal = 22)
    private BigDecimal actualWeight;

    @ApiModelProperty(value = "实际体积(m3)", position = 23)
    @JSONField(ordinal = 23)
    private BigDecimal actualVolume;

    @ApiModelProperty(value = "总箱数", position = 24)
    @JSONField(ordinal = 24)
    private Integer totalCartons;


    /*订单对应报关文件：order_customs_file*/
    @ApiModelProperty(value = "订单对应报关文件list", position = 25)
    @JSONField(ordinal = 25)
    private List<OrderCustomsFileVO> orderCustomsFileVOList;

    /*订单对应清关文件：order_clearance_file*/
    @ApiModelProperty(value = "订单对应清关文件list", position = 26)
    @JSONField(ordinal = 26)
    private List<OrderClearanceFileVO> orderClearanceFileVOList;

    /*订单商品list*/
    @ApiModelProperty(value = "订单商品list", position = 27)
    @JSONField(ordinal = 27)
    private List<OrderShopVO> orderShopVOList;

    /*订单对应箱号信息:order_case*/
    @ApiModelProperty(value = "订单箱号list", position = 28)
    @JSONField(ordinal = 28)
    private List<OrderCaseVO> orderCaseVOList;

    /*订单对应箱号配载信息:order_case、order_conf*/
    @ApiModelProperty(value = "订单对应箱号配载信息list", position = 29)
    @JSONField(ordinal = 29)
    private List<OrderCaseConfVO> orderCaseConfVOList;

    /*订单对应应收费用明细:order_cope_receivable*/
    @ApiModelProperty(value = "订单对应应收费用明细list", position = 30)
    @JSONField(ordinal = 30)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOList;

    /*订单对应应付费用明细:order_cope_with*/
    @ApiModelProperty(value = "订单对应应付费用明细list", position = 31)
    @JSONField(ordinal = 31)
    private List<OrderCopeWithVO> orderCopeWithVOList;

    /*订单对应提货信息表：order_pick*/
    @ApiModelProperty(value = "订单对应提货信息表list", position = 32)
    @JSONField(ordinal = 32)
    private List<OrderPickVO> orderPickVOList;

    /*订单关联运价(报价offer_info，报价模板quotation_template)*/
    @ApiModelProperty(value = "报价名称(商品名称)", position = 33)
    private String names;

    @ApiModelProperty(value = "类型1整柜 2散柜(货物类型)", position = 34)
    private Integer types;

    @ApiModelProperty(value = "开船日期", position = 35)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 35, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 36)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 36, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 37)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 37, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 38)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 38, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "预计到达时间", position = 39)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 39, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "航程(天)", position = 40)
    @JSONField(ordinal = 40)
    private String voyageDay;

    @ApiModelProperty(value = "操作信息(操作说明)", position = 41)
    @JSONField(ordinal = 41)
    private String remarks;

    @ApiModelProperty(value = "起运港(harbour_info id_code)", position = 42)
    @JSONField(ordinal = 42)
    private String startShipment;

    @ApiModelProperty(value = "目的港(harbour_info id_code)", position = 43)
    @JSONField(ordinal = 43)
    private String destinationPort;

    @ApiModelProperty(value = "起运港", position = 44)
    @JSONField(ordinal = 44)
    private String startShipmentName;

    @ApiModelProperty(value = "目的港", position = 45)
    @JSONField(ordinal = 45)
    private String destinationPortName;

    @ApiModelProperty(value = "报价模板id(quotation_template id)", position = 46)
    @JSONField(ordinal = 46)
    private Integer qie;

    //海运费：订柜尺寸(应收费用明细)
    @ApiModelProperty(value = "海运费：订柜尺寸(应收费用明细)", position = 47)
    @JSONField(ordinal = 47)
    private List<TemplateCopeReceivableVO> oceanFeeList;

    //内陆费：集货仓库(应收费用明细)
    @ApiModelProperty(value = "内陆费：集货仓库(应收费用明细)", position = 48)
    @JSONField(ordinal = 48)
    private List<TemplateCopeReceivableVO> inlandFeeList;

    //报价对应的目的仓库
    @ApiModelProperty(value = "报价对应的目的仓库", position = 49)
    @JSONField(ordinal = 49)
    private List<FabWarehouseVO> fabWarehouseVOList;

}
