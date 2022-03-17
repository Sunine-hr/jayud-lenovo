package com.jayud.wms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 入库报表数据实体类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Data
public class WarehouseingReportVO extends SysBaseEntity {

    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    private String ower;

    @ApiModelProperty(value = "所属仓库货架Code")
    private String shelfCode;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "数量")
    private Double num;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "推荐库位")
    private String recommendedLocation;

    @ApiModelProperty(value = "实际上架数量")
    private Double actualNum;

    @ApiModelProperty(value = "实际上架库位（库位基础数据编号）")
    private String actualShelfSpace;

    @ApiModelProperty(value = "起始工作站（基础数据编号）")
    private String startingStation;

    @ApiModelProperty(value = "上架执行人")
    private String shelfExecutor;

    @ApiModelProperty(value = "上架时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime shelfTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "状态(1:待上架,2:已上架)")
    private Integer status;

    @ApiModelProperty(value = "状态(1:待上架,2:已上架)")
    private String statusDesc;


    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDesc = this.status == 1 ? "待上架" : "已上架";
    }

}
