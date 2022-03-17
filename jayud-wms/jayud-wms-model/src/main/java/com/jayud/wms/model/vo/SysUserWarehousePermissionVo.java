package com.jayud.wms.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ciro
 * @date 2021/12/6 17:15
 * @description:
 */
@ApiModel(value="用户仓库关联vo", description="用户仓库关联vo")
@Data
public class SysUserWarehousePermissionVo {
    @ApiModelProperty(value = "关联主键id",name = "关联主键id")
    private String id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 货主id
     */
    @ApiModelProperty(value = "仓库id")
    private String warehouseId;
    /**
     * 货主名称
     */
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
    /**
     *租户编码
     */
    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    private Boolean isDeleted;

    /**
     * 是否选中
     */
    @ApiModelProperty(value = "是否选中")
    private Boolean isSelected;


}
