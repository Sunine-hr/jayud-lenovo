package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * SowingResults 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "播种结果对象", description = "播种结果")
@TableName(value = "wms_sowing_results")
public class SowingResults extends SysBaseEntity {

    @ApiModelProperty(value = "绑定播种id")
    private Long incomingSeedingId;

    @ApiModelProperty(value = "播种位编号")
    private String seedingPositionNum;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "原容器号")
    private String oldContainerNum;

    @ApiModelProperty(value = "数量")
    private Double oldNum;

    @ApiModelProperty(value = "播种数量")
    private Double sowingQuantity;

    @ApiModelProperty(value = "播种后数量")
    private Double quantityAfterSowing;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "新容器号")
    private String newContainerNum;

    @ApiModelProperty(value = "新的播种后数量")
    private Double newQuantityAfterSowing;

    @ApiModelProperty(value = "状态(1:未更换,2:已更换,3:确认上架,4:完成上架)")
    private Integer status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "新容器名称")
    private String newContainerName;


    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1(1:合格,2:不合格)(是否合格,可能后续上架策略会使用)")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;


}
