package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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

    @ApiModelProperty(value = "模板类型(1整柜 2拼箱)", position = 2)
    @JSONField(ordinal = 2)
    private Integer types;

    @ApiModelProperty(value = "服务分类(service_group id)", position = 3)
    @JSONField(ordinal = 3)
    @NotNull(message = "服务分类不能为空")
    private Integer sid;

    @ApiModelProperty(value = "报价模板名", position = 4)
    @JSONField(ordinal = 4)
    @NotNull(message = "报价模板名称不能为空")
    private String names;

    //@ApiModelProperty(value = "报价图片，多张用逗号分割", position = 5)
    //@JSONField(ordinal = 5)
    //private String picUrl;
    @ApiModelProperty(value = "报价图片，多张用逗号分割，数组", position = 5)
    @JSONField(ordinal = 5)
    private List<PicUrlArrForm> picUrlarr;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 6)
    @JSONField(ordinal = 6)
    @NotNull(message = "运输方式不能为空")
    private Integer tid;

    @ApiModelProperty(value = "起运港(harbour_info id_code)", position = 7)
    @JSONField(ordinal = 7)
    @NotNull(message = "起运港不能为空")
    private String startShipment;

    @ApiModelProperty(value = "目的港(harbour_info id_code)", position = 8)
    @JSONField(ordinal = 8)
    @NotNull(message = "目的港")
    private String destinationPort;

    //@ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔", position = 9)
    //@JSONField(ordinal = 9)
    //private String arriveWarehouse;
    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔，数组", position = 9)
    @JSONField(ordinal = 9)
    @NotNull(message = "可达仓库不能为空")
    private List<FabWarehouseForm> arriveWarehousearr;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 10)
    @JSONField(ordinal = 10)
    @NotNull(message = "可见客户不能为空")
    private Integer visibleCustomer;

    //@ApiModelProperty(value = "可见客户(customer.id，多客户时逗号分隔用户ID)", position = 11)
    //@JSONField(ordinal = 11)
    //private String visibleUid;
    @ApiModelProperty(value = "可见客户(customer.id，多客户时逗号分隔用户ID)，数组", position = 11)
    @JSONField(ordinal = 11)
    private List<CustomerForm> visibleUidarr;

    @ApiModelProperty(value = "开船日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 13, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 14, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "预计到达时间", position = 16)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 16, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 17)
    @JSONField(ordinal = 17)
    @NotNull(message = "货物类型不能为空")
    private Integer gidtype;

    //@ApiModelProperty(value = "货物类型(goods_type id),多个用逗号分隔", position = 17)
    //@JSONField(ordinal = 17)
    //private String gid;
    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔，数组", position = 18)
    @JSONField(ordinal = 18)
    private List<GoodsTypeForm> gidarr;

    //@ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 18)
    //@JSONField(ordinal = 18)
    //private String areaId;
    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 19)
    @JSONField(ordinal = 19)
    @NotNull(message = "集货仓库不能为空")
    private List<ShippingAreaForm> areaIdarr;

    @ApiModelProperty(value = "报价类型(1整柜 2拼箱)", position = 20)
    @JSONField(ordinal = 20)
    @NotNull(message = "报价类型不能为空")
    private Integer qidtype;

    //@ApiModelProperty(value = "报价类型(quotation_type id),多个用逗号分隔", position = 20)
    //@JSONField(ordinal = 20)
    //private String qid;
    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔，数组", position = 21)
    @JSONField(ordinal = 21)
    private List<QuotationTypeForm> qidarr;

    @ApiModelProperty(value = "任务分组id(运单任务task_group id)", position = 22)
    @JSONField(ordinal = 22)
    @NotNull(message = "任务分组id不能为空")
    private Integer taskId;

    @ApiModelProperty(value = "计泡系数(默认6000)", position = 23)
    @JSONField(ordinal = 23)
    @NotNull(message = "计泡系数不能为空")
    private BigDecimal bubbleCoefficient;

    @ApiModelProperty(value = "操作信息", position = 24)
    @JSONField(ordinal = 24)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 25)
    @JSONField(ordinal = 25)
    private String status;

    @ApiModelProperty(value = "创建人id(system_user id)", position = 26)
    @JSONField(ordinal = 26)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名(system_user name)", position = 27)
    @JSONField(ordinal = 27)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 28)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 28, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", position = 29)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 29, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /*应收费用明细List*/
    @ApiModelProperty(value = "应收费用明细List", position = 30)
    @JSONField(ordinal = 30)
    private List<TemplateCopeReceivableForm> templateCopeReceivableFormList;

    /*应付费用明细list*/
    @ApiModelProperty(value = "应付费用明细list", position = 31)
    @JSONField(ordinal = 31)
    private List<TemplateCopeWithForm> templateCopeWithFormList;

    /*文件信息明细list*/
    @ApiModelProperty(value = "文件信息明细list", position = 32)
    @JSONField(ordinal = 32)
    private List<TemplateFileForm> templateFileFormList;

}
