package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 入库订单驳回操作
 */
@Data
public class StorageOutCargoRejected {

    @ApiModelProperty(value = "快进快出订单id")
    @NotNull(message = "快进快出订单id不能为空")
    private Long storageInOrderId;

    @ApiModelProperty(value = "驳回原因")
    private String cause;

}
