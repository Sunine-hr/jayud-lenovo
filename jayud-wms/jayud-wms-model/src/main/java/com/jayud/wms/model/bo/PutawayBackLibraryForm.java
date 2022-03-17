package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 上架回库，货架回库，工作台收货上架
 */
@Data
public class PutawayBackLibraryForm {

    //仓库
    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    //工作台
    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    //货架
    @ApiModelProperty(value = "货架id")
    private Long shelfId;

    @ApiModelProperty(value = "货架编号")
    private String shelfCode;

    //收货信息
    @ApiModelProperty(value = "本次收货信息")
    private List<ReceivingInfoForm> receivingInfoList;

}
