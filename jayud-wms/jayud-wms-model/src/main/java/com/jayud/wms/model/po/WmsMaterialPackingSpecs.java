package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsMaterialPackingSpecs 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="物料-包装规格对象", description="物料-包装规格")
public class WmsMaterialPackingSpecs extends SysBaseEntity {


    @ApiModelProperty(value = "物料基本信息id")
    private Long materialBasicInfoId;

    @ApiModelProperty(value = "包装类型(1主单位，2小包装，3中包装，4大包装)")
    private int specsType;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "数量")
    private Integer account;

    @ApiModelProperty(value = "毛重(g)")
    @Digits(integer = 10, fraction = 2, message = "毛重保留小数点后两位！")
    private BigDecimal weight;

    @ApiModelProperty(value = "长度(cm)")
    @Digits(integer = 10, fraction = 2, message = "长度保留小数点后两位！")
    private BigDecimal length;

    @ApiModelProperty(value = "宽度(cm)")
    @Digits(integer = 10, fraction = 2, message = "宽度保留小数点后两位！")
    private BigDecimal width;

    @ApiModelProperty(value = "高度(cm)")
    @Digits(integer = 10, fraction = 2, message = "高度保留小数点后两位！")
    private BigDecimal height;

    @ApiModelProperty(value = "体积(cm3)")
    @Digits(integer = 10, fraction = 2, message = "体积保留小数点后两位！")
    private BigDecimal volume;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


    @ApiModelProperty(value = "包装类型名称")
    @TableField(exist = false)
    private String specsType_text;

    @ApiModelProperty(value = "物料id")
    @TableField(exist = false)
    private List<Long> materialIdList;






}
