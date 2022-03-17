package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2022/3/10 13:56
 * @description: 拣货下架信息VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="拣货下架信息VO", description="拣货下架信息VO")
public class WmsPackingOffshelfTaskVO {

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主编码")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    /**
     * 惠州道科
     */
    @ApiModelProperty(value = "所属仓库货架id")
    private Long shelfId;

    /**
     * 惠州道科
     */
    @ApiModelProperty(value = "所属货架code")
    private String shelfCode;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "库位ID")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "库位对象集合")
    private List<WarehouseLocationVO> locationList;

    @ApiModelProperty(value = "拣货下架对象集合")
    private List<WmsPackingOffshelfTask> taskList;

    @ApiModelProperty(value = "二维库位对象")
    private WarehouseLocationVO[][] locationArry;

}
