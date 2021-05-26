package com.jayud.storage.model.bo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单记录查询参数
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryInOrderForm extends BasePageForm {

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    @JsonIgnore
    private List<String> mainOrderNos;

    @ApiModelProperty(value = "主订单号")
    private String loginUserName;

    public void assemblyMainOrderNo(JSONArray mainOrders) {
        mainOrderNos = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            mainOrderNos.add(tmp.getStr("orderNo"));
        }
    }

}
