package com.jayud.wms.model.vo;


import com.jayud.wms.model.po.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author ciro
 * @date 2021/12/16 11:30
 * @description: 物料信息VO
 */
@Data
@Api(tags = "物料信息VO")
@EqualsAndHashCode(callSuper = false)
public class WmsMaterialBasicInfoVO extends WmsMaterialBasicInfo {
    /**
     * 是否新增
     */
    private Boolean isAdd;

    @ApiModelProperty(value = "物料类型名称")
    private String owerId_text;

    @ApiModelProperty(value = "物料类型名称")
    private String materialTypeId_text;

    @ApiModelProperty(value = "物料组名称")
    private String materialGroupId_text;

    @ApiModelProperty(value = "ABC标识名称")
    private String identificationId_text;

    @ApiModelProperty(value = "储存条件名称")
    private String storageConditionsId_text;

    @ApiModelProperty(value = "最小单位名称")
    private String minUnitId_text;

    @ApiModelProperty(value = "是否质检，0否，1是")
    private String isQualityInspection_text;

    @ApiModelProperty(value = "是否换容器，0否，1是")
    private String isReplaceContainer_text;

    @ApiModelProperty(value = "是否开启，0否，1是")
    private String isOn_text;

    @ApiModelProperty(value = "上架策略名称")
    private String shelfStrategyId_text;

    @ApiModelProperty(value = "分配策略名称")
    private String allocationStrategyId_text;

    @ApiModelProperty(value = "是否允许超收，0否，1是")
    private String isAllowOvercharge_text;

    @ApiModelProperty(value = "所有库位对象")
    private List<WmsMaterialToLoactionRelation> allLoactionSelect;

    @ApiModelProperty(value = "本次选中库位对象")
    private List<WmsMaterialToLoactionRelation> thisLoactionSelect;

    @ApiModelProperty(value = "上次选中库位对象")
    private List<WmsMaterialToLoactionRelation> lastLoactionSelect;

    @ApiModelProperty(value = "本次选中条形码对象")
    private List<WmsMaterialBarCode> thisBarCodeSelect;

    @ApiModelProperty(value = "上次选中条形码对象")
    private List<WmsMaterialBarCode> lastBarCodeSelect;

    @ApiModelProperty(value = "包装规格对象")
    private List<WmsMaterialPackingSpecs> packingList;

    @ApiModelProperty(value = "批属性配置")
    private List<WmsMaterialToAttribute> attributeList;

    Long[] ids;

    //通知单物料信息需要 修改成赋值字段
    @ApiModelProperty(value = "入库单物料类型")
    private String materialType;
    //通知单物料信息需要 修改成赋值字段
    @ApiModelProperty(value = "入库单物料单位")
    private String unit;

    @ApiModelProperty(value = "排除编码对象")
    private List<String> exitMaterialCodeList;

    @ApiModelProperty(value = "物料单位")
    private List<String> unitList;



    @ApiModelProperty(value = "负责、所属部门")
    private List<String> orgIds;


    @ApiModelProperty(value = "是否负责人")
    private Boolean isCharge;


    @ApiModelProperty(value = "负责货主id集合")
    private List<String> owerIdList;


    @ApiModelProperty(value = "负责仓库id集合")
    private List<String> warehouseIdList;
}
