package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
//fastjson 对象转 json 输出顺序
//@JSONType(orders = { "id", "types"})
public class QuotationTemplateForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2散柜)", position = 2)
//    @NotEmpty(message = "模板类型")
    @JSONField(ordinal = 2)
    private Integer types;

    @ApiModelProperty(value = "服务分类(service_group sid)", position = 3)
    @JSONField(ordinal = 3)
    private Integer sid;

    @ApiModelProperty(value = "报价名", position = 4)
    @JSONField(ordinal = 4)
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割，数组", position = 5)
    @JSONField(ordinal = 5)
    private List<PicUrlArrForm> picUrlarr;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer tid;

    @ApiModelProperty(value = "起运港", position = 7)
    @JSONField(ordinal = 7)
    private String startShipment;

    @ApiModelProperty(value = "目的港", position = 8)
    @JSONField(ordinal = 8)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔，数组", position = 9)
    @JSONField(ordinal = 9)
    private List<FabWarehouseForm> arriveWarehousearr;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 10)
    @JSONField(ordinal = 10)
    private Integer visibleCustomer;

    @ApiModelProperty(value = "可见客户(customer.id，多客户时逗号分隔用户ID)，数组", position = 11)
    @JSONField(ordinal = 11)
    private List<CustomerForm> visibleUidarr;

    @ApiModelProperty(value = "开船日期", position = 12)
    @JSONField(ordinal = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 13)
    @JSONField(ordinal = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 14)
    @JSONField(ordinal = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 15)
    @JSONField(ordinal = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 16)
    @JSONField(ordinal = 16)
    private Integer gidtype;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔，数组", position = 17)
    @JSONField(ordinal = 17)
    private List<GoodsTypeForm> gidarr;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 18)
    @JSONField(ordinal = 18)
    private List<ShippingAreaForm> areaIdarr;

    @ApiModelProperty(value = "报价类型(13整柜 18散柜)", position = 19)
    @JSONField(ordinal = 19)
    private Integer qidtype;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔，数组", position = 20)
    @JSONField(ordinal = 20)
    private List<GoodsTypeForm> qidarr;

    @ApiModelProperty(value = "任务分组id(task_group id)", position = 21)
    @JSONField(ordinal = 21)
    private Integer taskId;

    @ApiModelProperty(value = "计泡系数(默认6000)", position = 22)
    @JSONField(ordinal = 22)
    private BigDecimal bubbleCoefficient;

    @ApiModelProperty(value = "操作信息", position = 23)
    @JSONField(ordinal = 23)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 24)
    @JSONField(ordinal = 24)
    private String status;

    @ApiModelProperty(value = "创建人id", position = 25)
    @JSONField(ordinal = 25)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名", position = 26)
    @JSONField(ordinal = 26)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 27)
    @JSONField(ordinal = 27)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", position = 28)
    @JSONField(ordinal = 28)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /*应收费用明细List*/
    @ApiModelProperty(value = "应收费用明细List", position = 29)
    @JSONField(ordinal = 29)
    private List<TemplateCopeReceivableForm> templateCopeReceivableFormList;

    /*应付费用明细list*/
    @ApiModelProperty(value = "应付费用明细list", position = 30)
    @JSONField(ordinal = 30)
    private List<TemplateCopeWithForm> templateCopeWithFormList;

    /*文件信息明细list*/
    @ApiModelProperty(value = "文件信息明细list", position = 31)
    @JSONField(ordinal = 31)
    private List<TemplateFileForm> templateFileFormList;

}
