package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsAllocationStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ciro
 * @date 2022/1/17 10:18
 * @description: 分配策略VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "分配策略VO", description = "分配策略VO")
public class WmsAllocationStrategyVO extends WmsAllocationStrategy {

    @ApiModelProperty(value = "策略类型文本")
    private String allocationStrategyType_text;
}
