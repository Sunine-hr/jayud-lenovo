package com.jayud.finance.vo;

import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

/**
 * 该视图从其他表查询统计而来展示，后在保存到表中
 */
@Data
public class OrderReceiveBillVO {

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "已出账金额")
    private String alreadyPaidAmount;

    @ApiModelProperty(value = "已出账订单数")
    private Integer billOrderNum;

    @ApiModelProperty(value = "账单数")
    private Integer billNum;

    @ApiModelProperty(value = "未出账金额")
    private String notPaidAmount;

    @ApiModelProperty(value = "未出账订单数")
    private Integer notPaidOrderNum;

    @ApiModelProperty(value = "法人主体id")
    private Long legalEntityId;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;


    public void statisticsNotPaidBillInfo(List<Map<String, Object>> maps) {
        BigDecimal amount = new BigDecimal(0);
        Set<String> orderNos = new HashSet<>();
        for (Map<String, Object> map : maps) {
            orderNos.add(MapUtil.getStr(map, "orderNo"));
            BigDecimal changeAmount = map.get("changeAmount") == null ? new BigDecimal(0) : (BigDecimal) map.get("changeAmount");
            amount = amount.add(changeAmount);
        }
        this.notPaidOrderNum = orderNos.size();
        this.notPaidAmount = amount.toPlainString();
    }

}