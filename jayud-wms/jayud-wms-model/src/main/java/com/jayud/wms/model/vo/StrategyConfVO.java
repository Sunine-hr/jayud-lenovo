package com.jayud.wms.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * StrategyConf 实体类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="策略配置对象", description="策略配置")
public class StrategyConfVO extends SysBaseEntity {


    @ApiModelProperty(value = "上架策略id")
    private Long shelfStrategyId;

    @ApiModelProperty(value = "策略类型值(1:推荐库位,2:推荐库区,3:至库区)")
    private Integer type;

    @ApiModelProperty(value = "策略类型描述")
    private String typeDesc;

    @ApiModelProperty(value = "至库区所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "至库区所属仓库库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库位冻结状态(0:非冻结,1:冻结)")
    private Integer frozen;

    @ApiModelProperty(value = "库位类型1(字典值id)")
    private Long locationTypeOne;

    @ApiModelProperty(value = "库位类型2(字典值id)")
    private Long locationTypeTwo;

    @ApiModelProperty(value = "库位类型3(字典值id)")
    private Long locationTypeThree;

    @ApiModelProperty(value = "库位类型(多个用逗号隔开)")
    private String locationType;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "排序")
    private Integer sort;



}
