package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 货主和仓库关系 实体类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WmsOwerToWarehouseRelation对象", description="货主和仓库关系")
public class WmsOwerToWarehouseRelation extends SysBaseEntity {


    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;


    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;



}
