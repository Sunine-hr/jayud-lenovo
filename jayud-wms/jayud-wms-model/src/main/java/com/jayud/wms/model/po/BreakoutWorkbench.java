package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * BreakoutWorkbench 实体类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="工作台类型信息对象", description="工作台类型信息")
@TableName(value = "wms_breakout_workbench")
public class BreakoutWorkbench /*extends SysBaseEntity*/ {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    protected Long id;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "工作台类型(1:普通,2:分播,3:交接)")
    private Integer type;

    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;



}
