package com.jayud.tms.model.vo.supplier;

import com.jayud.tms.model.bo.BasePageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QuerySupplierBill extends BasePageForm {

    @ApiModelProperty("客户名称 (供应商是子订单操作主体)")
    private String customer;

    @ApiModelProperty("所属月份")
    private List<String> month;

    @ApiModelProperty("指派供应商id")
    private Long supplierId;
}
