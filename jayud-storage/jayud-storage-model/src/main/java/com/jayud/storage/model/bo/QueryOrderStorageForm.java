package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 入仓参数
 * </p>
 *
 * @author
 * @since
 */
@Data
public class QueryOrderStorageForm extends BasePageForm {

    @ApiModelProperty(value = "存仓订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "出库订单编号")
    private String outOrderNo;

    @ApiModelProperty(value = "所属月份")
    private String month;


}
