package com.jayud.wms.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * ShelfOrder 实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="上架单对象", description="上架单")
public class ShelfOrderVO extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "数量")
    private Double num;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "推荐库位")
    private String recommendedLocation;

    @ApiModelProperty(value = "任务明细号")
    private String taskDetailNum;

    @ApiModelProperty(value = "上架任务号(生成上架任务生成的)")
    private String shelfNum;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;



    @ApiModelProperty(value = "状态(1:待上架,2:已上架)")
    private Integer status;

    @ApiModelProperty(value = "状态(1:待上架,2:已上架)")
    private String statusDesc;


    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;

    @ApiModelProperty(value = "仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "货主名称")
    private String ower;


    public void setStatus(Integer status) {
        this.status = status;
        this.statusDesc = this.status == 1 ? "待上架" : "已上架";
    }

}
