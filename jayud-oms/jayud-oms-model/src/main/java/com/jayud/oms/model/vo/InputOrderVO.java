package com.jayud.oms.model.vo;

import com.jayud.oms.model.bo.InputStorageInputOrderForm;
import com.jayud.oms.model.bo.InputTrailerOrderFrom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class InputOrderVO {

    @ApiModelProperty(value = "主订单")
    private InputMainOrderVO orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsVO orderCustomsForm;


    @ApiModelProperty(value = "中港单")
    private InputOrderTransportVO orderTransportForm;

    @ApiModelProperty(value = "空运单")
    private InputAirOrderVO airOrderForm;

    @ApiModelProperty(value = "服务单")
    private InputOrderServiceVO orderServiceForm;

    @ApiModelProperty(value = "海运单")
    private InputSeaOrderVO seaOrderForm;

    @ApiModelProperty(value = "内陆单")
    private InputOrderInlandTPVO orderInlandTransportForm;

    @ApiModelProperty(value = "拖车单")
    private List<InputTrailerOrderVO> trailerOrderForm;

    @ApiModelProperty(value = "入库订单")
    private InputStorageInputOrderVO storageInputOrderForm;

    @ApiModelProperty(value = "出库订单")
    private InputStorageOutOrderVO storageOutOrderForm;

    @ApiModelProperty(value = "快进快出订单")
    private InputStorageFastOrderVO storageFastOrderForm;

    public void copyOperationInfo() {
        if (orderForm == null) return;
        orderForm.setOrderId(null);
        orderForm.setOrderNo(null);
        orderForm.setCreatedTimeStr(null);
        orderForm.setCreatedUser(null);
        orderForm.setStatus(null);
    }


}
