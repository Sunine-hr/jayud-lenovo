package com.jayud.oms.model.vo.template.order;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BaseOrderTemplate {

//    @ApiModelProperty(value = "订单编号(临时值)")
//    private String tmpOrderNo;

    @ApiModelProperty(value = "应收费用状态", required = true)
    private String receivableCostStatus;

    @ApiModelProperty(value = "应付费用状态", required = true)
    private String paymentCostStatus;

    /**
     * 重组费用状态
     */
    public void assembleCostStatus(String tmpOrderNo, Map<String, Object> costStatus) {
        Map<String, Object> receivableCostStatus = (Map<String, Object>) costStatus.get("receivableCostStatus");
        Map<String, Object> paymentCostStatus = (Map<String, Object>) costStatus.get("paymentCostStatus");

        String receivableStatusDesc = MapUtil.getStr(receivableCostStatus, tmpOrderNo);
        String paymentStatusDesc = MapUtil.getStr(paymentCostStatus, tmpOrderNo);

        if (!StringUtils.isEmpty(receivableStatusDesc)) {
            String[] split = receivableStatusDesc.split("-",2);
            this.receivableCostStatus = split[0];
        } else {
            this.receivableCostStatus = "未录入";
        }

        if (!StringUtils.isEmpty(paymentStatusDesc)) {
            String[] split = paymentStatusDesc.split("-",2);
            this.paymentCostStatus = split[0];
        } else {
            this.paymentCostStatus = "未录入";
        }
    }


}
