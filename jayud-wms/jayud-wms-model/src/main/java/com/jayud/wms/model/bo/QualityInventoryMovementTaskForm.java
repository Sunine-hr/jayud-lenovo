package com.jayud.wms.model.bo;

import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.InventoryMovementTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QualityInventoryMovementTaskForm {

    @ApiModelProperty(value = "移库类型代码(1物料移库，2容器移库，3库位移库)")
    private Integer movementTypeCode;

    @ApiModelProperty(value = "库存明细")
    private List<InventoryDetail> inventoryDetails;

    @ApiModelProperty(value = "移库任务")
    private List<InventoryMovementTask> movementTasks;

}
