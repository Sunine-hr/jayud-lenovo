package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * ShelfMoveTask 实体类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="货架移动任务对象", description="货架移动任务")
@TableName(value = "wms_shelf_move_task")
public class ShelfMoveTask extends SysBaseEntity {


    @ApiModelProperty(value = "移库类型代码(MTC01货架至工作台-收货,MTC02工作台至货架-上架,MTC03货架至工作台-下架,MTC04工作台至货架-下架,MTC05货架至工作台-盘点,MTC06工作台至货架-盘点)")
    private String movementTypeCode;

    @ApiModelProperty(value = "移库类型名称")
    private String movementTypeName;

    @ApiModelProperty(value = "货架移动任务号")
    private String mainCode;

    @ApiModelProperty(value = "货架移动任务明细号")
    private String mxCode;

    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "货架id")
    private Long shelfId;

    @ApiModelProperty(value = "货架编号")
    private String shelfCode;

    @ApiModelProperty(value = "订单来源(1系统创建 2人工创建)")
    private Integer orderSource;

    @ApiModelProperty(value = "订单状态(1待移动 2移动中 3已完成)")
    private Integer orderStatus;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
