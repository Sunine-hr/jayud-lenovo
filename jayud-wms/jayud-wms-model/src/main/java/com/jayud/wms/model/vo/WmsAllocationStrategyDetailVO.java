package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsAllocationStrategyDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ciro
 * @date 2022/1/17 13:33
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "分配策略详情VO", description = "分配策略详情VO")
public class WmsAllocationStrategyDetailVO extends WmsAllocationStrategyDetail {

    @ApiModelProperty(value = "策略类型文本")
    private String allocationStrategyType_text;
}
