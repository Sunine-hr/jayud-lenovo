package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * 入库节点操作流程
 */
@Data
@Slf4j
public class WarehousePickingForm {

    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不为空")
    private Long id;

    @ApiModelProperty(value = "出库订单号", required = true)
    @NotNull(message = "出库订单号不为空")
    private String orderNo;

    @ApiModelProperty(value = "sku")
    @NotNull(message = "sku不为空")
    private String sku;

    @ApiModelProperty(value = "库位编码")
    @NotNull(message = "库位编码不为空")
    private String kuCode;

    @ApiModelProperty(value = "待拣数量")
    @NotNull(message = "待拣数量不为空")
    private Integer number;

    @ApiModelProperty(value = "扫描库位编码")
    @NotNull(message = "扫描库位编码不为空")
    private String scanningKuCode;

    @ApiModelProperty(value = "已拣数量")
    @NotNull(message = "已拣数量不为空")
    private Integer pickedNumber;

    @ApiModelProperty(value = " 入库批次号")
    @NotNull(message = "入库批次号不为空")
    private String warehousingBatchNo;

    @ApiModelProperty(value = " 操作(确认submit 完结end)")
    @NotNull(message = "操作不为空")
    private String cmd;

}
