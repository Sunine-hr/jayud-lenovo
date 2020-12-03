package com.jayud.airfreight.model.bo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运订单列表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class QueryAirOrderForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空运订单编号")
    private String orderNo;

    @ApiModelProperty(value = "状态(k_0待接单,k_1空运接单,k_2订舱,k_3订单入仓, k_4确认提单,k_5确认离港,k_6确认到港,k_7海外代理k_8确认签收)")
    private String status;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "出发机场")
    private String portDeparture;

    @ApiModelProperty(value = "目的地机场")
    private String portDestination;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<String> mainOrderNos;

    public void assemblyMainOrderNo(JSONArray mainOrders) {
        mainOrderNos = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            mainOrderNos.add(tmp.getString("orderNo"));
        }
    }
}