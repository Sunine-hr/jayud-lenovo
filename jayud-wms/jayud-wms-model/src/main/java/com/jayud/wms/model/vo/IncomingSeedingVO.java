package com.jayud.wms.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * IncomingSeeding 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "入库播种对象", description = "入库播种")
public class IncomingSeedingVO extends SysBaseEntity {


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

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "数量")
    private Double num;

    @ApiModelProperty(value = "数量(总数/剩余数量)")
    private String numDesc;

    @ApiModelProperty(value = "已分配数量")
    private Double allocatedQuantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

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


    @ApiModelProperty(value = "新容器编号")
    private String newContainerNum;

    @ApiModelProperty(value = "新容器名称")
    private String newContainerName;

    @ApiModelProperty(value = "数量")
    private Double newNum;

    @ApiModelProperty(value = "播种位编号")
    private String seedingPositionNum;



}
