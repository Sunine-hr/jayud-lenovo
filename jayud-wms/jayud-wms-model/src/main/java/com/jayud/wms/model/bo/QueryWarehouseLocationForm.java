package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * WarehouseLocationForm 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="WarehouseLocation对象", description="库位信息")
public class QueryWarehouseLocationForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库库区id")
    private Long warehouseAreaId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "所属仓库货架id")
    private Long shelfId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "所属仓库货架Code")
    private String shelfCode;


    @ApiModelProperty(value = "库位编号")
    @TableField("`code`")
    private String code;

    @ApiModelProperty(value = "库位类型字典id")
    private Long type;

    @ApiModelProperty(value = "库位类型")
    private String typeDesc;

    @ApiModelProperty(value = "排数")
    @TableField("`row`")
    private Integer row;

    @ApiModelProperty(value = "列数")
    private Integer columnNum;

    @ApiModelProperty(value = "层数")
    private Integer layers;

    @ApiModelProperty(value = "长")
    private Double length;

    @ApiModelProperty(value = "宽")
    private Double wide;

    @ApiModelProperty(value = "高")
    private Double high;

    @ApiModelProperty(value = "是否允许混放")
    private Boolean isMixing;

    @ApiModelProperty(value = "是否入库冻结")
    private Boolean isInFrozen;

    @ApiModelProperty(value = "是否出库冻结")
    private Boolean isOutFrozen;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;




    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    @TableField("`status`")
    private Integer status;

    @ApiModelProperty(value = "混放策略id(字典获取)")
    private Long mixingStrategyId;

    @ApiModelProperty(value = "混放策略(字典获取)")
    private String mixingStrategy;

    @ApiModelProperty(value = "路线排序")
    private Integer routeSorting;


    //公司
    private List<String> orgIds;

    //是否在这个体系内
    private Boolean isCharge;

    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;
}
