package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkbenchCheckAffirmForm {

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

    //物料编号
    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    //数量
    @ApiModelProperty(value = "数量(盘点数量)")
    private BigDecimal checkCount;

    //库位编号
    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;
}
