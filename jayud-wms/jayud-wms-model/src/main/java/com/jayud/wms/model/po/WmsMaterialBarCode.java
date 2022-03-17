package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WmsMaterialBarCode 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="物料-条码对象", description="物料-条码")
public class WmsMaterialBarCode extends SysBaseEntity {


    @ApiModelProperty(value = "物料基本信息id")
    private Long materialBasicInfoId;

    @ApiModelProperty(value = "条码")
    private String barCode;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;






}
