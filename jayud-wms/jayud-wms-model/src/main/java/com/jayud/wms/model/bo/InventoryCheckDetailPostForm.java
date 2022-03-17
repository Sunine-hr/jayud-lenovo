package com.jayud.wms.model.bo;

import com.jayud.wms.model.po.InventoryCheckDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InventoryCheckDetailPostForm {

    @ApiModelProperty(value = "库存盘点ID")
    private Long inventoryCheckId;

    @ApiModelProperty(value = "库存盘点明细")
    private List<InventoryCheckDetail> details;
}
