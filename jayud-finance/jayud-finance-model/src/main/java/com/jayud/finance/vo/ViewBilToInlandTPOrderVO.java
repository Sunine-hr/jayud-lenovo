package com.jayud.finance.vo;

import cn.hutool.json.JSONObject;
import com.jayud.common.entity.OrderDeliveryAddress;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.vo.InlandTP.OrderInlandSendCarsVO;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 应收账单列表预览(内陆)
 */
@Data
public class ViewBilToInlandTPOrderVO {

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车型 如：3T")
    private String vehicleSize;

    @ApiModelProperty(value = "件数")
    private Integer pieceNum;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;


    public JSONObject assembleData(JSONObject jsonObject, List<OrderInlandTransportDetails> details) {
//        this.yunCustomsNo = jsonObject.getStr("yunCustomsNo");
//        setBillNo(jsonObject.getStr("billNo"));
//        setCreatedTimeStr(jsonObject.getStr("createdTimeStr"));
//        setOrderNo(jsonObject.getStr("orderNo"));
//        setSubOrderNo(jsonObject.getStr("subOrderNo"));
//        setUnitAccount(jsonObject.getStr("unitAccount"));
//        setTotalCost(jsonObject.get("totalCost") == null ? null : (List<String>) jsonObject.get("totalCost"));
//        setSettlementCurrencyCode(jsonObject.getStr("settlementCurrencyCode"));
//        setSettlementCurrency(jsonObject.getStr("settlementCurrency"));

        for (OrderInlandTransportDetails data : details) {
            if (jsonObject.getStr("subOrderNo").equals(data.getOrderNo())) {
                this.vehicleSize = data.getVehicleSize();
                //派车数据
                OrderInlandSendCarsVO sendCarsVO = data.getOrderInlandSendCarsVO();
                if (sendCarsVO != null) {
                    this.licensePlate = sendCarsVO.getLicensePlate();
                }
                List<OrderDeliveryAddress> pickUpAddressList = data.getPickUpAddressList();
                if (CollectionUtils.isNotEmpty(pickUpAddressList)) {
                    StringBuffer pickUpAddress = new StringBuffer();
                    StringBuffer goodsInfo = new StringBuffer();
                    Integer totalPieceNum = 0;
                    Double totalWeight = 0.0;
                    for (OrderDeliveryAddress orderDeliveryAddress : pickUpAddressList) {
                        pickUpAddress.append(orderDeliveryAddress.getAddress())
                                .append(",");
                        goodsInfo.append(orderDeliveryAddress.getGoodsName());
                        totalPieceNum += orderDeliveryAddress.getPlateAmount() == null ? 0 : orderDeliveryAddress.getPlateAmount();
                        totalWeight += orderDeliveryAddress.getTotalWeight() == null ? 0 : orderDeliveryAddress.getTotalWeight();
                    }
                    this.startAddress = pickUpAddress.toString();
                    this.goodsDesc = goodsInfo.toString();
                    this.pieceNum = totalPieceNum;
                    this.weight = totalWeight;
                }
                List<OrderDeliveryAddress> orderDeliveryAddresses = data.getOrderDeliveryAddressList();
                if (CollectionUtils.isNotEmpty(orderDeliveryAddresses)) {
                    StringBuffer sb = new StringBuffer();
                    for (OrderDeliveryAddress orderDeliveryAddress : orderDeliveryAddresses) {
                        sb.append(orderDeliveryAddress.getAddress())
                                .append(",");
                    }
                    this.endAddress = sb.toString();
                }

            }
        }
        JSONObject object = new JSONObject(this);
        object.putAll(jsonObject);
        return object;
    }
}
