package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value="QuotationTemplate对象", description="报价模板VO")
public class QuotationTemplateVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2散柜)")
    private Integer types;

    @ApiModelProperty(value = "报价名")
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割")
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)")
    private Integer tid;

    @ApiModelProperty(value = "起运港")
    private String startShipment;

    @ApiModelProperty(value = "目的港")
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔")
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(0所客户，多客户时逗号分隔用户ID)")
    private String visibleUid;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "货物类型(1普货 2特货)")
    private Integer goodsType;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔")
    private String areaId;

    @ApiModelProperty(value = "报价类型(1整柜 2散柜)")
    private Integer quoteType;

    @ApiModelProperty(value = "任务分组id(task_group id)")
    private Integer taskId;

    @ApiModelProperty(value = "操作信息")
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
