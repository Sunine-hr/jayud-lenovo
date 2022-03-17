package com.jayud.wms.model.vo;

import com.jayud.wms.model.bo.InventoryCheckDetailAppCompletedForm;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InventoryCheckAppVO extends SysBaseEntity {

    @ApiModelProperty(value = "盘点单号")
    private String checkCode;

    @ApiModelProperty(value = "盘点状态(1未盘点、2部分盘点、3已盘点)")
    private Integer checkStatus;

    @ApiModelProperty(value = "盘点类型(1明盘(显示账面数量) 2盲盘(不显示账面数量))")
    private Integer checkType;

    @ApiModelProperty(value = "总数")
    private Integer totalNum;

    @ApiModelProperty(value = "完成数量")
    private Integer completedNum;

    @ApiModelProperty(value = "最新的一条需要确认的盘点明细记录任务")
    private InventoryCheckDetailAppCompletedForm completed;

}
