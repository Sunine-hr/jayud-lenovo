package com.jayud.wms.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Container 实体类
 *
 */
@Data
public class ContainerVO extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "容器编号")
    private String code;

    @ApiModelProperty(value = "容器类型(直接是中文名字)")
    private String type;

    @ApiModelProperty(value = "长")
    private Double longs;

    @ApiModelProperty(value = "宽")
    private Double wide;

    @ApiModelProperty(value = "高")
    private Double high;

    @ApiModelProperty(value = "容器数量")
    private Long number;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Boolean status;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;
}
