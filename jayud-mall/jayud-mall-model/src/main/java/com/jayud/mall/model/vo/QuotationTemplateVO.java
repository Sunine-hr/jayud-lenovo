package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "自增id", position = 1)
    private Long id;

    @ApiModelProperty(value = "模板类型(1整柜 2散柜)", position = 2)
    private Integer types;

    @ApiModelProperty(value = "报价名", position = 3)
    private String names;

    @ApiModelProperty(value = "报价图片，多张用逗号分割", position = 4)
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 5)
    private Integer tid;

    @ApiModelProperty(value = "起运港", position = 6)
    private String startShipment;

    @ApiModelProperty(value = "目的港", position = 7)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔", position = 8)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 9)
    private Integer visibleCustomer;

    @ApiModelProperty(value = "可见客户(0所客户，多客户时逗号分隔用户ID)", position = 10)
    private String visibleUid;

    @ApiModelProperty(value = "开船日期", position = 11)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截仓日期", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "截亏仓日期", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 15)
    private Integer gidtype;

    @ApiModelProperty(value = "货物类型(goods_type id),多个用逗号分隔", position = 16)
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 17)
    private String areaId;

    @ApiModelProperty(value = "报价类型(1整柜 2散柜)", position = 18)
    private Integer qidtype;

    @ApiModelProperty(value = "报价类型(quotation_type id),多个用逗号分隔", position = 19)
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)", position = 20)
    private Integer taskId;

    @ApiModelProperty(value = "计泡系数(默认6000)", position = 21)
    private BigDecimal bubbleCoefficient;

    @ApiModelProperty(value = "操作信息", position = 22)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 23)
    private String status;

    @ApiModelProperty(value = "创建人id", position = 24)
    private Integer userId;

    @ApiModelProperty(value = "创建人姓名", position = 25)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 26)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", position = 27)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /*报价服务组:service_group*/
    @ApiModelProperty(value = "服务名称service_group.code_name", position = 28)
    private String sname;

    /*运输方式:transport_way*/
    @ApiModelProperty(value = "运输方式transport_way.code_name", position = 29)
    private String tname;

    /*机场、港口信息:harbour_info*/
    @ApiModelProperty(value = "起运港harbour_info.code_name", position = 30)
    private String startShipmentName;

    @ApiModelProperty(value = "目的港harbour_info.code_name", position = 31)
    private String destinationPortName;

    /*提单任务分组:task_group*/
    @ApiModelProperty(value = "关联任务task_group.code_name", position = 32)
    private String taskName;

    /**list**/

    /*可达仓库List*/
    @ApiModelProperty(value = "可达仓库List", position = 33)
    private List<FabWarehouseVO> fabWarehouseVOList;

    /*可见客户list*/
    @ApiModelProperty(value = "可见客户list", position = 34)
    private List<CustomerVO> customerVOList;

    /*货物类型list*/
    @ApiModelProperty(value = "货物类型list", position = 35)
    private List<GoodsTypeVO> gidarr;

    /*集货仓库list*/
    @ApiModelProperty(value = "集货仓库list", position = 36)
    private List<ShippingAreaVO> shippingAreaVOList;

    /*报价类型list*/
    @ApiModelProperty(value = "报价类型list", position = 37)
    private List<QuotationTypeVO> qidarr;

    /*报价对应应收费用明细list*/
    @ApiModelProperty(value = "报价对应应收费用明细list", position = 38)
    private List<TemplateCopeReceivableVO> templateCopeReceivableVOList;

    /*报价对应应付费用明细list*/
    @ApiModelProperty(value = "报价对应应付费用明细list", position = 39)
    private List<TemplateCopeWithVO> templateCopeWithVOList;

    /*模板对应模块信息list，文件信息*/
    @ApiModelProperty(value = "模板对应模块信息list，文件信息", position = 40)
    private List<TemplateFileVO> templateFileVOList;

    /*报价图片*/
    @ApiModelProperty(value = "报价图片，多张用逗号分割，数组", position = 41)
    private List<PicUrlArrForm> picUrlarr;

}
