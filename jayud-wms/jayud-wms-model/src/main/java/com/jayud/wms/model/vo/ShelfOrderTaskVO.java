package com.jayud.wms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * ShelfOrderTask 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上架任务单对象", description="上架任务单")
public class ShelfOrderTaskVO extends SysBaseEntity {


    @ApiModelProperty(value = "上架单id(shelf_order表)")
    private Long shelfOrderId;

    @ApiModelProperty(value = "上架任务号(生成上架任务生成的)")
    private String shelfNum;

    @ApiModelProperty(value = "任务明细号")
    private String taskDetailNum;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "待上架数量")
    private Double num;

    @ApiModelProperty(value = "实际上架数量")
    private Double actualNum;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "推荐库位（库位基础数据编号）")
    private String recommendedLocation;

    @ApiModelProperty(value = "实际上架库位（库位基础数据编号）")
    private String actualShelfSpace;

    @ApiModelProperty(value = "起始工作站（基础数据编号）")
    private String startingStation;

    @ApiModelProperty(value = "起始播种位（播种位基础数据编号）")
    private String startSeedingPosition;

    @ApiModelProperty(value = "上架执行人")
    private String shelfExecutor;

    @ApiModelProperty(value = "上架时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;




    @ApiModelProperty(value = "状态(1:待上架,2:上架中,3:已上架,4:强制上架,5:撤销上架)")
    private Integer status;

    @ApiModelProperty(value = "状态详情")
    private String statusDetails;


    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime productionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;

    @ApiModelProperty(value = "是否直接上架")
    private Boolean isPutShelf;


    @ApiModelProperty(value = "仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "货主名称")
    private String ower;

    @ApiModelProperty(value = "货主ID")
    private Long owerId;
}
