package com.jayud.oms.model.vo.cost;

import com.jayud.oms.model.vo.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CostOrderDetailsVO {

    @ApiModelProperty(value = "主订单")
    private CostMainOrderVO mainOrder;

    @ApiModelProperty(value = "中港单")
    private List<CostOrderTransportVO> orderTransportList = new ArrayList<>();

    public void assemblyData(InputOrderVO orderVO) {
        InputMainOrderVO orderForm = orderVO.getOrderForm();
        if (orderForm != null) {
            mainOrder = new CostMainOrderVO();
            mainOrder.setOrderNo(orderForm.getOrderNo());
            mainOrder.setCustomerName(orderForm.getCustomerName());
            mainOrder.setLegalName(orderForm.getLegalName());
            mainOrder.setUnitAccount(orderForm.getUnitAccount());
            //中港
            InputOrderTransportVO orderTransportForm = orderVO.getOrderTransportForm();
            if (orderTransportForm != null) {
                CostOrderTransportVO orderTransport = new CostOrderTransportVO();
                orderTransport.assemblyData(orderTransportForm);
//                orderTransport.setOrderNo(orderTransportForm.getOrderNo());
//                orderTransport.setVehicleSize(orderTransportForm.getVehicleSize());
//                orderTransport.assemblyTakeAdr(orderTransportForm.getOrderTakeAdrForms1());
                orderTransportList.add(orderTransport);
            }
        }
    }
}
