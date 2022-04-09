package com.jayud.auth.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysUserToWarehouse 实体类
 *
 * @author jayud
 * @since 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户与仓库关联表对象", description="用户与仓库关联表")
public class SysUserToWarehouseForm extends SysBaseEntity {


    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;






    @ApiModelProperty(value = "删除状态：0-未删除，1-已删除")
    @TableLogic
    private Boolean isDeleted;

}
