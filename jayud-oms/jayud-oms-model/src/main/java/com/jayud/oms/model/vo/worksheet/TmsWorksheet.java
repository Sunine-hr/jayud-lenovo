package com.jayud.oms.model.vo.worksheet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

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
    private Integer totalNum;

    @ApiModelProperty(value = "提货总重量")
    private String totalWeight;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "费用明细")
    private List<CostDetailsWorksheet> costDetails;
}
