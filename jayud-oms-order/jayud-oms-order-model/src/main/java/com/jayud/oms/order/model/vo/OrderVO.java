package com.jayud.oms.order.model.vo;

import com.jayud.oms.order.model.bo.CrmFileForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class OrderVO {

    @ApiModelProperty(value = "主订单", required = true)
    private OmsOrderVO orderForm;

    @ApiModelProperty(value = "报关单")
    private OrderCustomsVO orderCustomsForm;

    @ApiModelProperty(value = "中港")
    private OrderTransportVO orderTransportForm;

    @ApiModelProperty(value = "空运")
    private AirOrderVO airOrderForm;

    @ApiModelProperty(value = "服务单")
    private OrderServiceVO orderServiceForm;

    @ApiModelProperty(value = "海运")
    private SeaOrderVO seaOrderForm;

    @ApiModelProperty(value = "内陆")
    private OrderInlandTransportVO orderInlandTransportForm;

    @ApiModelProperty(value = "拖车")
    private List<TrailerOrderVO> trailerOrderFrom;

    @ApiModelProperty(value = "入库")
    private StorageInputOrderVO storageInputOrderForm;

    @ApiModelProperty(value = "出库")
    private StorageOutOrderVO storageOutOrderForm;

    @ApiModelProperty(value = "快进快出")
    private StorageOrderVO storageFastOrderForm;

    @ApiModelProperty(value = "附件列表")
    private List<CrmFileForm> crmFileForms;

    @ApiModelProperty(value = "商品明细")
    private List<OmsOrderEntryVO> omsOrderEntryForms;

    @ApiModelProperty(value = "操作记录")
    private List<OmsOrderFollowVO> omsOrderFollowVOS;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;



}
