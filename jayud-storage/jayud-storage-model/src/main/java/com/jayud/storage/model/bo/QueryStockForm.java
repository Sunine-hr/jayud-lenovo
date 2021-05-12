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
 * 入仓参数
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryStockForm extends BasePageForm {

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户id集合")
    private List<String> customerIds;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "库位id")
    private Long kuId;

    public void assemblyMainOrderNo(JSONArray mainOrders) {
        customerIds = new ArrayList<>(mainOrders.size());
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject tmp = mainOrders.getJSONObject(i);
            customerIds.add(tmp.getStr("id"));
        }
    }
}
