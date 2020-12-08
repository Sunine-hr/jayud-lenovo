package com.jayud.mall.model.vo;

import com.jayud.mall.model.bo.PicUrlArrForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)")
    private Integer visibleCustomer;

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

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔")
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔")
    private String areaId;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔")
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)")
    private Integer taskId;

    @ApiModelProperty(value = "计泡系数(默认6000)")
    private BigDecimal bubbleCoefficient;

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

    /**list**/

    /*可达仓库List*/
    @ApiModelProperty(value = "可达仓库List")
    private List<FabWarehouseVO> fabWarehouseVOList;

    /*可见客户list*/
    @ApiModelProperty(value = "可见客户list")
    private List<CustomerVO> customerVOList;

    /*货物类型list*/
    @ApiModelProperty(value = "货物类型list")
    private List<GoodsTypeVO> gidarr;

    /*集货仓库list*/
    @ApiModelProperty(value = "集货仓库list")
    private List<ShippingAreaVO> shippingAreaVOList;

    /*报价类型list*/
    @ApiModelProperty(value = "报价类型list")
    private List<QuotationTypeVO> qidarr;

    /*报价对应应收费用明细list*/
    @ApiModelProperty(value = "报价对应应收费用明细list")
    private List<TemplateCopeReceivableVO> templateCopeReceivableVOList;

    /*报价对应应付费用明细list*/
    @ApiModelProperty(value = "报价对应应付费用明细list")
    private List<TemplateCopeWithVO> templateCopeWithVOList;

    /*模板对应模块信息list，文件信息*/
    @ApiModelProperty(value = "模板对应模块信息list，文件信息")
    private List<TemplateFileVO> templateFileVOList;

    /*报价图片*/
    @ApiModelProperty(value = "报价图片，多张用逗号分割，数组", position = 5)
    private List<PicUrlArrForm> picUrlarr;


}
