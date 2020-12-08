package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.bo.PicUrlArrForm;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "报价-截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "报价-截仓日期", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "报价-截亏仓日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预计到达时间", position = 13)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedTime;

    @ApiModelProperty(value = "操作信息", position = 14)
    private String remarks;

    /*航程*/
    @ApiModelProperty(value = "航程(预计到达时间estimated_time - 开船日期sail_time)", position = 15)
    private String voyageDay;

    /*起运港-->目的港*/
    @ApiModelProperty(value = "起运港-->目的港", position = 16)
    private String shipRoute;

    /**关联字段显示**/
    /*报价模板quotation_template*/
    @ApiModelProperty(value = "服务分类(service_group sid)", position = 17)
    private Integer sid;

    @ApiModelProperty(value = "报价图片，多张用逗号分割", position = 18)
    private String picUrl;

    @ApiModelProperty(value = "运输方式(transport_way id)", position = 19)
    private Integer tid;

    @ApiModelProperty(value = "起运港", position = 20)
    private String startShipment;

    @ApiModelProperty(value = "目的港", position = 21)
    private String destinationPort;

    @ApiModelProperty(value = "可达仓库(fab_warehouse.id),多个用逗号分隔", position = 22)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可见客户(0公开 1指定客户)", position = 23)
    private Integer visibleCustomer;

    @ApiModelProperty(value = "可见客户(custome.id，多客户时逗号分隔用户ID)", position = 24)
    private String visibleUid;

    @ApiModelProperty(value = "货物类型(1普货 2特货)", position = 25)
    private Integer gidtype;

    @ApiModelProperty(value = "货物类型(goods_type types=2 id),多个用逗号分隔", position = 26)
    private String gid;

    @ApiModelProperty(value = "集货仓库(shipping_area id),多个都号分隔", position = 27)
    private String areaId;

    @ApiModelProperty(value = "报价类型(1整柜 2散柜)", position = 28)
    private Integer qidtype;

    @ApiModelProperty(value = "报价类型(goods_type types=1 id),多个用逗号分隔", position = 29)
    private String qid;

    @ApiModelProperty(value = "任务分组id(task_group id)", position = 30)
    private Integer taskId;

    @ApiModelProperty(value = "操作信息", position = 31)
    private String templateRemarks;

    /*报价服务组:service_group*/
    @ApiModelProperty(value = "服务名称service_group.code_name", position = 32)
    private String sname;

    /*运输方式:transport_way*/
    @ApiModelProperty(value = "运输方式transport_way.code_name", position = 33)
    private String tname;

    /*机场、港口信息:harbour_info*/
    @ApiModelProperty(value = "起运港harbour_info.code_name", position = 34)
    private String startShipmentName;

    @ApiModelProperty(value = "目的港harbour_info.code_name", position = 35)
    private String destinationPortName;

    /*提单任务分组:task_group*/
    @ApiModelProperty(value = "关联任务task_group.code_name", position = 36)
    private String taskName;

    /**关联list显示**/
    /*可达仓库List*/
    @ApiModelProperty(value = "可达仓库List", position = 37)
    private List<FabWarehouseVO> fabWarehouseVOList;

    /*可见客户list*/
    @ApiModelProperty(value = "可见客户list", position = 38)
    private List<CustomerVO> customerVOList;

    /*货物类型list*/
    @ApiModelProperty(value = "货物类型list", position = 39)
    private List<GoodsTypeVO> gidarr;

    /*集货仓库list*/
    @ApiModelProperty(value = "集货仓库list", position = 40)
    private List<ShippingAreaVO> shippingAreaVOList;

    /*报价类型list*/
    @ApiModelProperty(value = "报价类型list", position = 41)
    private List<QuotationTypeVO> qidarr;

    /*报价对应应收费用明细list*/
    @ApiModelProperty(value = "报价对应应收费用明细list", position = 42)
    private List<TemplateCopeReceivableVO> templateCopeReceivableVOList;

    /*报价对应应付费用明细list*/
    @ApiModelProperty(value = "报价对应应付费用明细list", position = 43)
    private List<TemplateCopeWithVO> templateCopeWithVOList;

    /*模板对应模块信息list，文件信息*/
    @ApiModelProperty(value = "模板对应模块信息list，文件信息", position = 44)
    private List<TemplateFileVO> templateFileVOList;

    /*配载单，关联查询报价信息-路线*/
    @ApiModelProperty(value = "配载单，关联查询报价信息-路线", position = 45)
    private String route;

    //海运费：订柜尺寸(应收费用明细)
    @ApiModelProperty(value = "海运费：订柜尺寸(应收费用明细)", position = 46)
    private List<TemplateCopeReceivableVO> oceanFeeList;

    //内陆费：集货仓库(应收费用明细)
    @ApiModelProperty(value = "内陆费：集货仓库(应收费用明细)", position = 46)
    private List<TemplateCopeReceivableVO> inlandFeeList;

    //图片
    @ApiModelProperty(value = "图片", position = 47)
    private List<PicUrlArrForm> picUrlarr;
}
