package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.InventoryCheckDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作台盘点
 */
@Data
public class WorkbenchCheckVO {

    //工作台
    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    //货架
    @ApiModelProperty(value = "货架id")
    private Long shelfId;

    @ApiModelProperty(value = "货架id")
    private String shelfCode;

    @ApiModelProperty(value = "货架示意图")
    private List<WarehouseShelfSketchMapVO> shelfSketchMaps;

    @ApiModelProperty(value = "库存盘点明细list")
    private List<InventoryCheckDetail> inventoryCheckDetails;

}
