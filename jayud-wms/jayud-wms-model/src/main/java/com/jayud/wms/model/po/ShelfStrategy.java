package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ShelfStrategy 实体类
 *
 * @author jyd
 * @since 2022-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上架策略对象", description="上架策略")
@TableName(value = "wms_shelf_strategy")
public class ShelfStrategy extends SysBaseEntity {


    @ApiModelProperty(value = "上架策略名称")
    private String name;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
