package com.jayud.oms.model.vo.gps;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * gps订单基本信息
 */
@Data
@Accessors(chain = true)
public class GPSOrderInfoVO {

    @ApiModelProperty(value = "子订单id")
    private Long subOrderId;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "开始时间(例如:中港提货时间)")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间(例如:中港签收时间)")
    private LocalDateTime endTime;

    public void assemblyTmsOrder(Object data) {
        Optional<Object> opt = Optional.ofNullable(data);
        opt.ifPresent(e -> {
            JSONObject jsonObject = new JSONObject(e);
            this.mainOrderNo = jsonObject.getStr("mainOrderNo");
            this.subOrderId = jsonObject.getLong("id");
            this.customerName = jsonObject.getStr("customerName");
            JSONArray pickUpAddressArray = jsonObject.getJSONArray("pickUpAddress");
            JSONObject pickUpAddress = pickUpAddressArray.getJSONObject(0);
            this.startTime = pickUpAddress.get("takeTimeStr", LocalDateTime.class);
            this.goodsInfo = pickUpAddress.getStr("goodsInfo");
            JSONObject orderSendCars = jsonObject.getJSONObject("orderSendCars");
            this.driverName = orderSendCars.getStr("driverName");
            this.plateNumber = orderSendCars.getStr("plateNumber");
        });

    }
}
