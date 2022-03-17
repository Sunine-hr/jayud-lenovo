package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysUserWarehousePermission 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUserWarehousePermission对象", description="用户与仓库权限关联表")
public class SysUserWarehousePermission extends SysBaseEntity {


    /**
     * 用户id
     */
    private String userId;

    @ApiModelProperty(value = "仓库id")
    private String warehouseId;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;



    /**
     * 是否选中
     */
    @TableField(exist = false)
    private Boolean isSelected;

    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;

}
