package com.jayud.mall.model.bo;

import lombok.Data;

import java.util.List;

@Data
public class OrderInfoCostForm {

    /*订单对应应收费用明细:order_cope_receivable*/
    private List<OrderCopeReceivableForm> orderCopeReceivableVOList;

    /*订单对应应付费用明细:order_cope_with*/
    private List<OrderCopeWithForm> orderCopeWithVOList;

}
