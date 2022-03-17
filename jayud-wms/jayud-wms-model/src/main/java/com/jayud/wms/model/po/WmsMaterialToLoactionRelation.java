package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * WmsMaterialToLoactionRelation 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="物料和库位关系", description="物料和库位关系")
public class WmsMaterialToLoactionRelation extends SysBaseEntity {


    @ApiModelProperty(value = "物料基本信息id")
    private Long materialBasicInfoId;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "库位id")
    private Long localtionId;

    @ApiModelProperty(value = "库位编号")
    private String localtionCode;

    @ApiModelProperty(value = "库位名称")
    private String localtionName;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;






}
