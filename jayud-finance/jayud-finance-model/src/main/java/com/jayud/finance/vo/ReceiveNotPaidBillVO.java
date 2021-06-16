package com.jayud.finance.vo;

import com.jayud.finance.vo.InlandTP.OrderInlandSendCarsVO;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 未出账订单数列表
 */
@Data
public class ReceiveNotPaidBillVO {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "子订单编号")
    private String subOrderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCodeDesc;

    @ApiModelProperty(value = "创建日期")
    private String createdTimeStr;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    @ApiModelProperty(value = "费用类型")
    private String costGenreName;

    @ApiModelProperty(value = "费用类别")
    private String costTypeName;

    @ApiModelProperty(value = "费用名称")
    private String costName;

//    @ApiModelProperty(value = "人民币")
//    private BigDecimal rmb;
//
//    @ApiModelProperty(value = "美元")
//    private BigDecimal dollar;
//
//    @ApiModelProperty(value = "欧元")
//    private BigDecimal euro;
//
//    @ApiModelProperty(value = "港币")
//    private BigDecimal hKDollar;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "本币代码")
    private String currencyCode;

    @ApiModelProperty(value = "应收金额描述")
    private String amountStr;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "费用备注")
    private String remarks;

    @ApiModelProperty(value = "费用类型/类别/名称维度的本币金额,不显示")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "应付费用ID,不显示")
    private Long costId;

    @ApiModelProperty(value = "车型 如：3T,不显示")
    private String vehicleSize;

    @ApiModelProperty(value = "订单维度的件数,不显示")
    private Integer pieceNum;

    @ApiModelProperty(value = "订单维度的重量,不显示")
    private Double weight;

    @ApiModelProperty(value = "1-暂存 2-生成账单")
    private String isBill;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    public void assembleInlandTPData(List<OrderInlandTransportDetails> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        for (OrderInlandTransportDetails data : dataList) {
            if (this.subOrderNo.equals(data.getOrderNo())) {
                //派车数据
                OrderInlandSendCarsVO sendCarsVO = data.getOrderInlandSendCarsVO();
                if (sendCarsVO != null) {
                    this.licensePlate = sendCarsVO.getLicensePlate();
                }

            }
        }
    }


    public void assembleAmountStr(String currencyName) {
        this.amountStr = this.amount.toPlainString()  + " " + currencyName;
    }
}
