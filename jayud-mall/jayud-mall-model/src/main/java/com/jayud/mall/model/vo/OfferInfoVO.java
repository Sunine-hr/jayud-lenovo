package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.bo.PicUrlArrForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value="报价对象", description="报价VO")
public class OfferInfoVO {

    /*报价*/
    @ApiModelProperty(value = "报价-自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价-报价模板id(quotation_template id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "报价-报价名称", position = 3)
    @JSONField(ordinal = 3)
    private String names;

    @ApiModelProperty(value = "报价-开船日期", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 4)
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "报价-截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 5)
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "报价-截仓日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 6)
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "报价-截亏仓日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 7)
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "报价-类型(1整柜 2拼箱)", position = 8)
    @JSONField(ordinal = 8)
    private Integer types;

    @ApiModelProperty(value = "报价-状态(0无效 1有效)", position = 9)
    @JSONField(ordinal = 9)
    private String status;

    @ApiModelProperty(value = "报价-创建人id(system_user id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer userId;

    @ApiModelProperty(value = "报价-创建人姓名(system_user name)", position = 11)
    @JSONField(ordinal = 11)
    private String userName;

    @ApiModelProperty(value = "报价-创建时间", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 12)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预计到达时间", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(ordinal = 13)
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息", position = 14)
    @JSONField(ordinal = 14)
    private String remarks;

    @ApiModelProperty(value = "开船日期备注")
    private String sailTimeRemark;

    @ApiModelProperty(value = "特别说明")
    private String specialVersion;

    @ApiModelProperty(value = "报价单号")
    private String offerNo;

    /*航程*/
    @ApiModelProperty(value = "航程(预计到达时间estimated_time - 开船日期sail_time)", position = 15)
    @JSONField(ordinal = 15)
    private String voyageDay;

    /*起运港-->目的港*/
    @ApiModelProperty(value = "起运港-->目的港", position = 16)
    @JSONField(ordinal = 16)
    private String shipRoute;

    /**关联字段显示**/
    /*报价模板quotation_template*/
    @ApiModelProperty(value = "服务分类(service_group sid)", position = 17)
    @JSONField(ordinal = 17)
    private Integer sid;

    @ApiModelProperty(value = "报价图片，多张用逗号分割", position = 18)
    @JSONField(ordinal = 18)
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 19)
    @JSONField(ordinal = 19)
    private Integer tid;

    @ApiModelProperty(value = "起运港", position = 20)
    @JSONField(ordinal = 20)
    private String startShipment;

    @ApiModelProperty(value = "目的港", position = 21)
    @JSONField(ordinal = 21)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔", position = 22)
    @JSONField(ordinal = 22)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 23)
    @JSONField(ordinal = 23)
    private Integer visibleCustomer;

    @ApiModelProperty(value = "可见客户(custome.id，多客户时逗号分隔用户ID)", position = 24)
    @JSONField(ordinal = 24)
    private String visibleUid;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 25)
    @JSONField(ordinal = 25)
    private Integer gidtype;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔", position = 26)
    @JSONField(ordinal = 26)
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 27)
    @JSONField(ordinal = 27)
    private String areaId;

    @ApiModelProperty(value = "报价类型(1整柜 2拼箱)", position = 28)
    @JSONField(ordinal = 28)
    private Integer qidtype;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔", position = 29)
    @JSONField(ordinal = 29)
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)", position = 30)
    @JSONField(ordinal = 30)
    private Integer taskId;

    @ApiModelProperty(value = "操作信息", position = 31)
    @JSONField(ordinal = 31)
    private String templateRemarks;

    @ApiModelProperty(value = "计泡系数(默认6000)", position = 23)
    @JSONField(ordinal = 23)
    private BigDecimal bubbleCoefficient;

    @ApiModelProperty(value = "计费重单位(1柜 2KG 3CBM 4车)")
    private Integer billingWeightUnit;

    @ApiModelProperty(value = "计算公式\n" +
            "1材积->重量：长*高*宽/计泡系数(单位KG)\n" +
            "2重量->材积：实重/计泡系数(单位CBM)")
    private Integer designFormulas;

    @ApiModelProperty(value = "最低数量")
    private BigDecimal minimumQuantity;

    @ApiModelProperty(value = "结算方式id(clearing_way id)")
    private Integer clearingWay;

    @ApiModelProperty(value = "下单页面显示 结算方式id(clearing_way id)")
    private Integer showClearingWay;

    /*报价服务组:service_group*/
    @ApiModelProperty(value = "服务名称service_group.code_name", position = 32)
    @JSONField(ordinal = 32)
    private String sname;

    /*运输方式:transport_way*/
    @ApiModelProperty(value = "运输方式transport_way.code_name", position = 33)
    @JSONField(ordinal = 33)
    private String tname;

    /*机场、港口信息:harbour_info*/
    @ApiModelProperty(value = "起运港harbour_info.code_name", position = 34)
    @JSONField(ordinal = 34)
    private String startShipmentName;

    @ApiModelProperty(value = "目的港harbour_info.code_name", position = 35)
    @JSONField(ordinal = 35)
    private String destinationPortName;

    /*提单任务分组:task_group*/
    @ApiModelProperty(value = "关联任务task_group.code_name", position = 36)
    @JSONField(ordinal = 36)
    private String taskName;

    /**关联list显示**/
    /*可达仓库List*/
    @ApiModelProperty(value = "可达仓库List", position = 37)
    @JSONField(ordinal = 37)
    private List<FabWarehouseVO> fabWarehouseVOList;

    /*可见客户list*/
    @ApiModelProperty(value = "可见客户list", position = 38)
    @JSONField(ordinal = 38)
    private List<CustomerVO> customerVOList;

    /*货物类型list*/
    @ApiModelProperty(value = "货物类型list", position = 39)
    @JSONField(ordinal = 39)
    private List<GoodsTypeVO> gidarr;

    /*集货仓库list*/
    @ApiModelProperty(value = "集货仓库list", position = 40)
    @JSONField(ordinal = 40)
    private List<ShippingAreaVO> shippingAreaVOList;

    /*报价类型list*/
    @ApiModelProperty(value = "报价类型list", position = 41)
    @JSONField(ordinal = 41)
    private List<QuotationTypeVO> qidarr;

    /*报价对应应收费用明细list*/
    @ApiModelProperty(value = "报价对应应收费用明细list", position = 42)
    @JSONField(ordinal = 42)
    private List<TemplateCopeReceivableVO> templateCopeReceivableVOList;

    /*报价对应应付费用明细list*/
    @ApiModelProperty(value = "报价对应应付费用明细list", position = 43)
    @JSONField(ordinal = 43)
    private List<TemplateCopeWithVO> templateCopeWithVOList;

    /*模板对应模块信息list，文件信息*/
    @ApiModelProperty(value = "模板对应模块信息list，文件信息", position = 44)
    @JSONField(ordinal = 44)
    private List<TemplateFileVO> templateFileVOList;

    /*配载单，关联查询报价信息-路线*/
    @ApiModelProperty(value = "配载单，关联查询报价信息-路线", position = 45)
    @JSONField(ordinal = 45)
    private String route;

    //rec应收费用信息
    //rec1.海运费：订柜尺寸(应收费用明细)
    @ApiModelProperty(value = "海运费：订柜尺寸(应收费用明细)", position = 46)
    @JSONField(ordinal = 46)
    private List<TemplateCopeReceivableVO> oceanFeeList;

    //rec2.内陆费：集货仓库(应收费用明细)
    @ApiModelProperty(value = "内陆费：集货仓库(应收费用明细)", position = 47)
    @JSONField(ordinal = 47)
    private List<TemplateCopeReceivableVO> inlandFeeList;

    //rec3.其他应收费用
    @ApiModelProperty(value = "其他费用：其他(应收费用明细)", position = 47)
    @JSONField(ordinal = 47)
    private List<TemplateCopeReceivableVO> otherFeeList;

    //图片
    @ApiModelProperty(value = "图片", position = 48)
    @JSONField(ordinal = 48)
    private List<PicUrlArrForm> picUrlarr;

    //目的港，对应的国家名称
    @ApiModelProperty(value = "目的港，对应的国家名称", position = 49)
    @JSONField(ordinal = 49)
    private String destinationCountryName;

    @ApiModelProperty(value = "操作说明list", position = 50)
    @JSONField(ordinal = 50)
    private List<String> remarksList;

    @ApiModelProperty(value = "运价对应目的仓库代码(s)", position = 51)
    @JSONField(ordinal = 51)
    private String arriveWarehouseCodes;

    //设置报价的海运费 最小值到最大值
    @ApiModelProperty(value = "价格(报价的海运费 最小值到最大值)", position = 52)
    @JSONField(ordinal = 52)
    private String amountRange;

    @ApiModelProperty(value = "容量(数值) quotation_template volume")
    private BigDecimal volume;

    @ApiModelProperty(value = "容量单位(1KG 2CBM) quotation_template volume_unit")
    private Integer volumeUnit;

    @ApiModelProperty(value = "整体时效(quotation_template whole_time)")
    private String wholeTime;

}
