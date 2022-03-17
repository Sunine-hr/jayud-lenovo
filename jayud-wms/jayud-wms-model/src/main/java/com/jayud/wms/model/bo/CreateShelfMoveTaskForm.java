package com.jayud.wms.model.bo;

import com.jayud.wms.model.po.WarehouseShelf;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 创建货架移动任务 form
 */
@Data
public class CreateShelfMoveTaskForm {

    @ApiModelProperty(value = "移库类型代码(MTC01货架至工作台-收货,MTC02工作台至货架-上架,MTC03货架至工作台-下架,MTC04工作台至货架-下架,MTC05货架至工作台-盘点,MTC06工作台至货架-盘点)")
    private String movementTypeCode;

    @ApiModelProperty(value = "移库类型名称")
    private String movementTypeName;

    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    @ApiModelProperty(value = "货架信息List")
    private List<WarehouseShelf> warehouseShelfList;

}
