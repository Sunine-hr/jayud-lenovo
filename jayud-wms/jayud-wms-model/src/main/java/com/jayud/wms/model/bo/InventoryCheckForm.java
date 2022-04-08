package com.jayud.wms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryCheckForm extends SysBaseEntity {


    @ApiModelProperty(value = "盘点单号")
    private String checkCode;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "盘点状态(1未盘点、2部分盘点、3已盘点)")
    private Integer checkStatus;

    @ApiModelProperty(value = "盘点类型(1明盘(显示账面数量) 2盲盘(不显示账面数量))")
    private Integer checkType;

    @ApiModelProperty(value = "库存数量")
    private BigDecimal inventoryCount;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal checkCount;

    @ApiModelProperty(value = "盘点人")
    private String checkBy;

    @ApiModelProperty(value = "盘点开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkStartTime;

    @ApiModelProperty(value = "盘点完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkFinishTime;

    @ApiModelProperty(value = "更新开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateStartTime;

    @ApiModelProperty(value = "更新完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateFinishTime;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    //库区 or 物料
    @ApiModelProperty(value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;


    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "工作台编号")
    private String workbenchCode;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "货架id")
    private Long shelfId;

    /************************* 惠州道科-业务 新增字段 @MFC 2022年3月5日09:41:49 *************************************/
    @ApiModelProperty(value = "货架编号")
    private String shelfCode;


    @ApiModelProperty(value = "货主ID")
    private Long owerId;

    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

//    @ApiModelProperty(value = "库区ID")
//    private Long warehouseAreaId;
//
//    @ApiModelProperty(value = "库区编号")
//    private String warehouseAreaCode;
//
//    @ApiModelProperty(value = "库区名称")
//    private String warehouseAreaName;

}
