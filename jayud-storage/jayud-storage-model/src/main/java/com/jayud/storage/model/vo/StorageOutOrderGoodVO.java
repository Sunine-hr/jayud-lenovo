package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
public class StorageOutOrderGoodVO {

    @ApiModelProperty(value = "默认出库时间")
    private String defaultOutTime;

    @ApiModelProperty(value = "默认批次号")
    private String defaultBatchNo;

    @ApiModelProperty(value = "出库商品信息")
    private List<WarehouseGoodsLocationVO> warehouseGoodsLocationVOS;

    @ApiModelProperty(value = "出库商品信息")
    private List<WarehouseGoodsLocationCodeVO> warehouseGoodsLocationCodeVOS;

    @ApiModelProperty(value = "是否可完成拣货")
    private boolean isFinishPicking;
}
