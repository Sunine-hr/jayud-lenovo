package com.jayud.mall.model.vo;

import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.model.bo.TemplateFileForm;
import com.jayud.mall.model.po.*;
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

    /*报价模板*/
    @ApiModelProperty(value = "报价模板-服务分类(service_group sid)", position = 13)
    private Integer sid;

    @ApiModelProperty(value = "服务分类List", position = 14)
    private List<ServiceGroup> serviceGroupList;

    @ApiModelProperty(value = "报价模板-报价图片，多张用逗号分割", position = 15)
    private String picUrl;

    @ApiModelProperty(value = "报价图片List", position = 16)
    private List<String> picUrlList;

    @ApiModelProperty(value = "报价模板-运输方式(transport_way id)", position = 17)
    private Integer tid;

    @ApiModelProperty(value = "运输方式List", position = 18)
    private List<TransportWay> transportWayList;

    @ApiModelProperty(value = "报价模板-起运港", position = 19)
    private String startShipment;

    @ApiModelProperty(value = "报价模板-目的港", position = 20)
    private String destinationPort;

    @ApiModelProperty(value = "报价模板-可达仓库(fab_warehouse.id),多个用逗号分隔", position = 21)
    private String arriveWarehouse;

    @ApiModelProperty(value = "可达仓库list", position = 22)
    private List<FabWarehouse> fabWarehouseList;

    @ApiModelProperty(value = "报价模板-可见客户(custome.id，多客户时逗号分隔用户ID)", position = 23)
    private String visibleUid;

    @ApiModelProperty(value = "可见客户客户list", position = 24)
    private List<Customer> customerList;

    @ApiModelProperty(value = "报价模板-货物类型(goods_type types=2 id),多个用逗号分隔", position = 25)
    private String gid;

    @ApiModelProperty(value = "货物类型", position = 26)
    private List<GoodsType> gList;

    @ApiModelProperty(value = "报价模板-集货仓库(shipping_area id),多个都号分隔", position = 27)
    private String areaId;

    @ApiModelProperty(value = "集货仓库list", position = 28)
    private List<ShippingArea> shippingAreaList;

    @ApiModelProperty(value = "报价模板-报价类型(goods_type types=1 id),多个用逗号分隔", position = 29)
    private String qid;

    @ApiModelProperty(value = "报价类型List", position = 30)
    private List<GoodsType> qList;

    @ApiModelProperty(value = "报价模板-任务分组id(task_group id)", position = 31)
    private Integer taskId;

    @ApiModelProperty(value = "任务分组List", position = 32)
    private List<TaskGroup> taskGroupsList;

    @ApiModelProperty(value = "报价模板-操作信息", position = 33)
    private String remarks;

    /*应收费用明细List*/
    @ApiModelProperty(value = "应收费用明细List", position = 34)
    private List<TemplateCopeReceivableForm> templateCopeReceivableFormList;

    /*应付费用明细list*/
    @ApiModelProperty(value = "应付费用明细list", position = 35)
    private List<TemplateCopeWithForm> templateCopeWithFormList;

    /*文件信息明细list*/
    @ApiModelProperty(value = "文件信息明细list", position = 36)
    private List<TemplateFileForm> templateFileFormList;

}
