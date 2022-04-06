package com.jayud.wms.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ciro
 * @date 2021/12/20 14:37
 * @description: 出库通知单-物料信息VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库通知单-物料信息VO", description="出库通知单-物料信息VO")
public class WmsOutboundNoticeOrderInfoToMaterialVO extends WmsOutboundNoticeOrderInfoToMaterial {



    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型编码")
    private String materialTypeCode;

    @ApiModelProperty(value = "物料类型名称")
    private String materialTypeId_text;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "货主ID")
    private Long owerId;

    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "现有量")
    private BigDecimal existingCount;

    @ApiModelProperty(value = "分配量")
    private BigDecimal allocationCount;

    @ApiModelProperty(value = "拣货量")
    private BigDecimal pickingCount;

    @JsonProperty(value = "inWarehouseTime")
    @ApiModelProperty(value = "上架时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inWarehouseTime;


}
