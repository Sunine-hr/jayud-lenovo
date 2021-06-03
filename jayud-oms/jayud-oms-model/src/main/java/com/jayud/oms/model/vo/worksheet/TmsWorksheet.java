package com.jayud.oms.model.vo.worksheet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputMainOrderVO;
import com.jayud.oms.model.vo.InputOrderTakeAdrVO;
import com.jayud.oms.model.vo.InputOrderTransportVO;
import com.jayud.oms.model.vo.InputOrderVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 中港工作表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TmsWorksheet {

    @ApiModelProperty(value = "客户名称")
    private String customer;

    @ApiModelProperty(value = "操作人")
    private String createdUser;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号")
    private String subOrderNo;

    @ApiModelProperty(value = "日期")
    private String takeTime;

    @ApiModelProperty(value = "提货总件数")
    private Double totalNum;

    @ApiModelProperty(value = "提货总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "费用明细")
    private List<CostDetailsWorksheet> reCostDetails;

    @ApiModelProperty(value = "费用明细")
    private List<CostDetailsWorksheet> payCostDetails;

    @ApiModelProperty(value = "利润")
    private BigDecimal profit;

    public TmsWorksheet assemblyData(InputOrderVO data) {
        if (data.getOrderForm() == null || data.getOrderTransportForm() == null) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
        InputOrderTransportVO transportForm = data.getOrderTransportForm();
//        JSONObject jsonObject = new JSONObject(data);
//        this.customer = jsonObject.getStr("customerName");
//        this.createdUser = jsonObject.getStr("createdUser");
//        this.mainOrderNo = jsonObject.getStr("mainOrderNo");
//        this.subOrderNo = jsonObject.getStr("orderNo");
//        this.takeTime = jsonObject.getJSONArray("pickUpAddress")
        InputMainOrderVO orderForm = data.getOrderForm();
        this.customer = orderForm.getCustomerName().split("\\(")[0];
        this.createdUser = orderForm.getCreatedUser();
        this.mainOrderNo = orderForm.getOrderNo();
        this.bizType = orderForm.getBizDesc();
        this.subOrderNo = transportForm.getOrderNo();


        assemblyTakeAdr(transportForm.getOrderTakeAdrForms1());
        return this;
    }

    public void assemblyTakeAdr(List<InputOrderTakeAdrVO> orderTakeAdrForms2) {
        if (CollectionUtil.isEmpty(orderTakeAdrForms2)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        Double totalNum = 0.0;
        Double totalWeight = 0.0;
        for (InputOrderTakeAdrVO inputOrderTakeAdrVO : orderTakeAdrForms2) {
            totalNum += inputOrderTakeAdrVO.getPieceAmount();
            totalWeight += inputOrderTakeAdrVO.getWeight();
        }
        this.totalNum = totalNum;
        this.totalWeight = totalWeight;
        this.takeTime = DateUtils.format(orderTakeAdrForms2.get(0).getTakeTimeStr(), DateUtils.DATE_PATTERN);
    }

    public void assemblyCost(List<OrderReceivableCost> receivableCosts,
                             List<OrderPaymentCost> paymentCosts,
                             Map<String, String> costInfoMap) {
        List<CostDetailsWorksheet> reCostDetails = new ArrayList<>();
        List<CostDetailsWorksheet> payCostDetails = new ArrayList<>();
        Map<String, BigDecimal> totalReCost = new HashMap<>();
        Map<String, BigDecimal> totalPayCost = new HashMap<>();
        BigDecimal profit = new BigDecimal(0);
        for (OrderReceivableCost receivableCost : receivableCosts) {
            CostDetailsWorksheet costDetail = new CostDetailsWorksheet();
            costDetail.setReCustomer(receivableCost.getCustomerName())
                    .setReCost(costInfoMap.get(receivableCost.getCostCode()))
                    .setReCurrency(receivableCost.getCurrencyCode())
                    .setReAmount(receivableCost.getAmount());
            totalReCost.merge(receivableCost.getCurrencyCode(), receivableCost.getAmount(), BigDecimal::add);
            reCostDetails.add(costDetail);
            profit = profit.add(receivableCost.getChangeAmount());
        }

        for (OrderPaymentCost paymentCost : paymentCosts) {
            CostDetailsWorksheet costDetail = new CostDetailsWorksheet();
            costDetail.setReCustomer(paymentCost.getCustomerName())
                    .setReCost(costInfoMap.get(paymentCost.getCostCode()))
                    .setReCurrency(paymentCost.getCurrencyCode())
                    .setReAmount(paymentCost.getAmount());
            totalPayCost.merge(paymentCost.getCurrencyCode(), paymentCost.getAmount(), BigDecimal::add);
            payCostDetails.add(costDetail);
            profit = profit.subtract(paymentCost.getChangeAmount());
        }
        this.profit = profit;
        this.reCostDetails = reCostDetails.size() > 13 ? reCostDetails.subList(0, 13) : reCostDetails;
        this.payCostDetails = payCostDetails.size() > 13 ? payCostDetails.subList(0, 13) : payCostDetails;
    }
}
