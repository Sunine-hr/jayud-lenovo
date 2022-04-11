package com.jayud.wms.model.bo.LargeScreen;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2022/4/11 10:58
 * @description:
 */
@Data
@ApiModel(value = "仓库信息", description = "仓库信息")
public class WarehouseForm {

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "年月：yyyy-mm")
    private String yearMonth;

    @ApiModelProperty(value = "是否完成")
    private Boolean isFinish;

}
