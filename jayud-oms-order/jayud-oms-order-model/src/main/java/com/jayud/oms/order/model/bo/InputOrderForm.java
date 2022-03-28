package com.jayud.oms.order.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class InputOrderForm {

    @ApiModelProperty(value = "主订单", required = true)
    private OmsOrderForm orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsForm orderCustomsForm;

    @ApiModelProperty(value = "中港")
    private InputOrderTransportForm orderTransportForm;

    @ApiModelProperty(value = "空运")
    private InputAirOrderForm airOrderForm;

    @ApiModelProperty(value = "服务单")
    private InputOrderServiceForm orderServiceForm;

    @ApiModelProperty(value = "海运")
    private InputSeaOrderForm seaOrderForm;

    @ApiModelProperty(value = "内陆")
    private InputOrderInlandTransportForm orderInlandTransportForm;

    @ApiModelProperty(value = "拖车")
    private List<InputTrailerOrderFrom> trailerOrderFrom;

    @ApiModelProperty(value = "入库")
    private InputStorageInputOrderForm storageInputOrderForm;

    @ApiModelProperty(value = "出库")
    private InputStorageOutOrderForm storageOutOrderForm;

    @ApiModelProperty(value = "快进快出")
    private InputStorageFastOrderForm storageFastOrderForm;

    @ApiModelProperty(value = "附件列表")
    private List<CrmFileForm> crmFileForms;

    @ApiModelProperty(value = "商品明细")
    private List<InputOmsOrderEntryForm> omsOrderEntryForms;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;



}
