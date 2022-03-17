package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.BreakoutWorkbench;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WorkbenchVO extends SysBaseEntity {

    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库库区")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "工作台编号")
    private String code;

    @ApiModelProperty(value = "工作台名称")
    private String name;

    @ApiModelProperty(value = "工作台类型(1:普通,2:分播,3:交接)")
    private Integer type;

    @ApiModelProperty(value = "AGV排队上限")
    private Integer agvNum;

    @ApiModelProperty(value = "排队货架类型(中文值)")
    private String queueShelfType;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "工作台类型信息")
    private List<BreakoutWorkbench> breakoutWorkbenches;

}
