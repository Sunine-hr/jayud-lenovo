package com.jayud.Inlandtransport.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 订单驳回操作
 */
@Data
public class OrderRejectedOpt {

    @ApiModelProperty(value = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "驳回原因")
    private String cause;

    @ApiModelProperty(value = "驳回选项(1:订单驳回,2:派车驳回)")
    @JsonIgnore
    private Integer rejectOptions;

    @ApiModelProperty(value = "删除状态集合")
    @JsonIgnore
    private List<String> deleteStatusList;
}
