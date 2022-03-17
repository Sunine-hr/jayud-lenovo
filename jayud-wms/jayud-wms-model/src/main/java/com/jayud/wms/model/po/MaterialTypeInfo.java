package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 物料类型信息
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MaterialTypeInfo对象", description = "物料类型信息")
@TableName(value = "wms_material_type_info")
public class MaterialTypeInfo extends SysBaseEntity {

    @ApiModelProperty(value = "上级ID")
    private Long parentId;

    @ApiModelProperty(value = "物料类型编码")
    private String materialTypeCode;

    @ApiModelProperty(value = "物料类型名称")
    private String materialTypeName;

    @ApiModelProperty(value = "排序")
    private Long order;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "备注信息")
    private String remark;

}
