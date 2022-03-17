package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WmsMaterialToAttribute 实体类
 *
 * @author jyd
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="物料管理-批属性配置对象", description="物料管理-批属性配置")
public class WmsMaterialToAttribute extends SysBaseEntity {


    @ApiModelProperty(value = "物料基本信息id")
    private Long materialBasicInfoId;

    @ApiModelProperty(value = "物料基本信息编码")
    private String materialCode;

    @ApiModelProperty(value = "批属性类型(1-批属性1,2-批属性2,3-批属性3,4-批属性4,5-批属性5)")
    private Integer attributeType;

    @ApiModelProperty(value = "是否可见")
    private Boolean isVisible;

    @ApiModelProperty(value = "是否必填")
    private Boolean isRequired;

    @ApiModelProperty(value = "属性名称")
    private String attributeNanme;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "批属性类型文本")
    private String attributeType_text;






    }
