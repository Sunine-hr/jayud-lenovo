package com.jayud.wms.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/14 9:15
 * @description:
 */
@Data
@ApiModel(value="用户数据权限vo", description="用户数据权限vo")
public class SysDataPermissionVo {

    @ApiModelProperty(value = "用户id")
    private String userId;


    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "货主集合")
    List<SysUserOwerPermissionVo> owerList;

    @ApiModelProperty(value = "仓库集合")
    List<SysUserWarehousePermissionVo> warehouseList;

    /**
     * 上次选中货主id集合
     */
    @ApiModelProperty(value = "上次选中货主id集合")
    private List<String> lastSelectOwerIds;

    /**
     * 本次选中货主id集合
     */
    @ApiModelProperty(value = "本次选中货主id集合")
    private List<String> thisSelectOwerIds;

    /**
     * 上次选中仓库id集合
     */
    @ApiModelProperty(value = "上次选中仓库id集合")
    private List<String> lastSelectWarehouseIds;

    /**
     * 本次选中仓库id集合
     */
    @ApiModelProperty(value = "本次选中仓库id集合")
    private List<String> thisSelectWarehouseIds;


}
