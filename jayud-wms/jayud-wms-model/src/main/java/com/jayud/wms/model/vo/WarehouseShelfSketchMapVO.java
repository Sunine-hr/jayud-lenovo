package com.jayud.wms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 货架示意图，货架下的库位使用情况
 */
@Data
public class WarehouseShelfSketchMapVO {

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    /**
     * 入库
     *     工作台收货上架
     *
     *     白色:库位为空    white
     *     橙色:本次已收货使用    orange
     *     灰色:之前收货已使用    gray
     *
     * 出库
     *     工作台拣货出库
     *
     *     白色:本次无关库位    white
     *     橙色:待拣货下架库位    orange
     *     绿色: 已拣货下架库位    green
     *
     * 盘点
     *     工作台盘点
     *
     *     白色:本次无关库位    white
     *     橙色:待拣货下架库位    orange
     *     绿色: 已拣货下架库位    green
     *
     * @see com.jayud.model.enums..ColorCodeEnum
     */
    @ApiModelProperty(value = "库位颜色代码")
    private String colorCode;

    @ApiModelProperty(value = "料号 数量")
    private List<MaterialDetailVO> materialDetails;

}
