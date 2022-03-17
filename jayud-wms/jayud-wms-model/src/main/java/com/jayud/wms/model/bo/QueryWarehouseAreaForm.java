package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * WarehouseAreaForm 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WarehouseArea对象", description="库区信息")
public class QueryWarehouseAreaForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "库区编号")
    private String code;

    @ApiModelProperty(value = "库区名称")
    private String name;

    @ApiModelProperty(value = "库区类型(中文值)")
    private String typeDesc;

    @ApiModelProperty(value = "是否允许混放")
    private Boolean isMixing;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;




    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "混放策略id(字典获取)")
    private Long mixingStrategyId;

    @ApiModelProperty(value = "混放策略(字典获取)")
    private String mixingStrategy;

    //公司
    private List<String> orgIds;

    //是否在这个体系内
    private Boolean isCharge;

    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;

}
