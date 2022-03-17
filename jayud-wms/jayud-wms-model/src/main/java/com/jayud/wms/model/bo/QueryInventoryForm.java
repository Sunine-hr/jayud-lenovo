package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QueryInventoryForm {

    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库id集合")
    private List<Long> warehouseIds;

    @ApiModelProperty(value = "所属仓库库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库位编号")
    private String code;

    @ApiModelProperty(value = "需要过滤的ids")
    private List<Long> notIds;

}
