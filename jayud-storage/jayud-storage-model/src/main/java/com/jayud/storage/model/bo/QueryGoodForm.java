package com.jayud.storage.model.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QueryGoodForm extends BasePageForm{

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<Long> customerIds;

    public void assemblyCustomerIds(JSONArray mainOrders) {
        customerIds = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            customerIds.add(tmp.getLong("id"));
        }
    }
}
