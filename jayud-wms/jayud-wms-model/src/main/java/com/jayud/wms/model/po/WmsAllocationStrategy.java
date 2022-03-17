package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * WmsAllocationStrategy 实体类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="分配策略对象", description="分配策略")
public class WmsAllocationStrategy extends SysBaseEntity {


    @ApiModelProperty(value = "分配策略编号")
    private String allocationStrategyCode;

    @ApiModelProperty(value = "分配策略名称")
    private String allocationStrategyName;


    @ApiModelProperty(value = "排序")
    private Integer sorts;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






    }
