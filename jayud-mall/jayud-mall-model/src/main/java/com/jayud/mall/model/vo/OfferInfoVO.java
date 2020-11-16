package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value="报价对象", description="报价VO")
public class OfferInfoVO {

    /*报价*/
    @ApiModelProperty(value = "报价-自增加id", position = 1)
    private Long id;

    @ApiModelProperty(value = "报价-报价模板id(quotation_template)", position = 2)
    private Integer qie;

    @ApiModelProperty(value = "报价-报价名称", position = 3)
    private String names;

    @ApiModelProperty(value = "报价-开船日期", position = 4)
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "报价-截单日期", position = 5)
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "报价-截仓日期", position = 6)
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "报价-截亏仓日期", position = 7)
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "报价-类型1整柜 2散柜", position = 8)
    private Integer types;

    @ApiModelProperty(value = "报价-状态(0无效 1有效)", position = 9)
    private String status;

    @ApiModelProperty(value = "报价-创建人id", position = 10)
    private Integer userId;

    @ApiModelProperty(value = "报价-创建人姓名", position = 11)
    private String userName;

    @ApiModelProperty(value = "报价-创建时间", position = 12)
    private LocalDateTime createTime;

    /**关联字段显示**/
    /*报价模板quotation_template*/
    @ApiModelProperty(value = "服务分类(service_group sid)")
    private Integer sid;

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

    @ApiModelProperty(value = "可见客户(custome.id，多客户时逗号分隔用户ID)")
    private String visibleUid;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔")
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔")
    private String areaId;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔")
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)")
    private Integer taskId;

    @ApiModelProperty(value = "操作信息")
    private String remarks;

    /*报价服务组:service_group*/
    @ApiModelProperty(value = "服务名称service_group.code_name")
    private String sName;

    /*运输方式:transport_way*/
    @ApiModelProperty(value = "运输方式transport_way.code_name")
    private String tName;

    /*机场、港口信息:harbour_info*/
    @ApiModelProperty(value = "起运港harbour_info.code_name")
    private String startShipmentName;

    @ApiModelProperty(value = "目的港harbour_info.code_name")
    private String destinationPortName;

    /*提单任务分组:task_group*/
    @ApiModelProperty(value = "关联任务task_group.code_name")
    private String taskName;

    /**关联list显示**/
    /*可达仓库List*/
    @ApiModelProperty(value = "可达仓库List")
    private List<FabWarehouseVO> fabWarehouseVOList;

    /*可见客户list*/
    @ApiModelProperty(value = "可见客户list")
    private List<CustomerVO> customerVOList;

    /*货物类型list*/
    @ApiModelProperty(value = "货物类型list")
    private List<GoodsTypeVO> gList;

    /*集货仓库list*/
    @ApiModelProperty(value = "集货仓库list")
    private List<ShippingAreaVO> shippingAreaVOList;

    /*报价类型list*/
    @ApiModelProperty(value = "报价类型list")
    private List<GoodsTypeVO> qList;

    /*报价对应应收费用明细list*/
    private List<TemplateCopeReceivableVO> templateCopeReceivableVOList;

    /*报价对应应付费用明细list*/
    private List<TemplateCopeWithVO> templateCopeWithVOList;

    /*模板对应模块信息list，文件信息*/
    private List<TemplateFileVO> templateFileVOList;


}
