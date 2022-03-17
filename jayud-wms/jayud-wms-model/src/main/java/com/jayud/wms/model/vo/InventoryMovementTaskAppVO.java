package com.jayud.wms.model.vo;

import com.jayud.wms.model.bo.InventoryMovementTaskAppCompletedForm;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InventoryMovementTaskAppVO extends SysBaseEntity {

    @ApiModelProperty(value = "移库类型代码(1物料移库，2容器移库，3库位移库)")
    private Integer movementTypeCode;

    @ApiModelProperty(value = "移库类型名称")
    private String movementTypeName;

    @ApiModelProperty(value = "主任务号")
    private String mainCode;

    @ApiModelProperty(value = "总数")
    private Integer totalNum;

    @ApiModelProperty(value = "完成数量")
    private Integer completedNum;

    @ApiModelProperty(value = "最新的一条需要确认的移库任务")
    private InventoryMovementTaskAppCompletedForm completed;

}
