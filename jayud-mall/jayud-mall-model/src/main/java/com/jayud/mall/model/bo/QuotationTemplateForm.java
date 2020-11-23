package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
//fastjson 对象转 json 输出顺序
//@JSONType(orders = { "id", "types"})
public class QuotationTemplateForm {

    @ApiModelProperty(value = "自增id")
    @JSONField(ordinal = 0)
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2散柜)")
//    @NotEmpty(message = "模板类型")
    @JSONField(ordinal = 1)
    private Integer types;

    @ApiModelProperty(value = "服务分类(service_group sid)")
    @JSONField(ordinal = 2)
    private Integer sid;

    @ApiModelProperty(value = "报价名")
    @JSONField(ordinal = 3)
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割")
    @JSONField(ordinal = 4)
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    @JSONField(ordinal = 5)
    private Integer tid;

    @ApiModelProperty(value = "起运港")
    @JSONField(ordinal = 6)
    private String startShipment;

    @ApiModelProperty(value = "目的港")
    @JSONField(ordinal = 7)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔")
    @JSONField(ordinal = 8)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(custome.id，多客户时逗号分隔用户ID)")
    @JSONField(ordinal = 9)
    private String visibleUid;

    @ApiModelProperty(value = "开船日期")
    @JSONField(ordinal = 10)
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期")
    @JSONField(ordinal = 11)
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期")
    @JSONField(ordinal = 12)
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期")
    @JSONField(ordinal = 13)
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔")
    @JSONField(ordinal = 14)
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔")
    @JSONField(ordinal = 15)
    private String areaId;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔")
    @JSONField(ordinal = 16)
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)")
    @JSONField(ordinal = 17)
    private Integer taskId;

    @ApiModelProperty(value = "操作信息")
    @JSONField(ordinal = 18)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    @JSONField(ordinal = 19)
    private String status;

    @ApiModelProperty(value = "创建人id")
    @JSONField(ordinal = 20)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名")
    @JSONField(ordinal = 21)
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JSONField(ordinal = 22)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @JSONField(ordinal = 23)
    private LocalDateTime updateTime;

    /*应收费用明细List*/
    @ApiModelProperty(value = "应收费用明细List")
    @JSONField(ordinal = 24)
    private List<TemplateCopeReceivableForm> templateCopeReceivableFormList;

    /*应付费用明细list*/
    @ApiModelProperty(value = "应付费用明细list")
    @JSONField(ordinal = 25)
    private List<TemplateCopeWithForm> templateCopeWithFormList;

    /*文件信息明细list*/
    @ApiModelProperty(value = "文件信息明细list")
    @JSONField(ordinal = 26)
    private List<TemplateFileForm> templateFileFormList;

}
