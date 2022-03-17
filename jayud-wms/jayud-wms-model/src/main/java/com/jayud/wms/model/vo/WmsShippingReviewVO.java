package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * WmsShippingReview 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
public class WmsShippingReviewVO extends SysBaseEntity {

    @ApiModelProperty(value = "物料明细id")
    private Long allocationId;

    @ApiModelProperty(value = "波次单号")
    private String wareNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "是否完成(0否，1是，2关箱)")
    private Integer isEnd;

    @ApiModelProperty(value = "拣货下架单id")
    private Long packingOffshelfId;

    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;
    @ApiModelProperty(value = "拣货下架单明细号")
    private String packingOffshelfDetailNumber;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主编码")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "拣货数量")
    private Integer packingAccount;

    @ApiModelProperty(value = "已扫描数量")
    private Integer scannedAccount;

    @ApiModelProperty(value = "未扫描数量")
    private Integer notScannedAccount;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "复核人")
    private String reviewerBy;

    @ApiModelProperty(value = "复核时间")
    private LocalDateTime reviewerTime;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "生产时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime materialProductionDate;

    @ApiModelProperty(value = "自定义1")
    private String customField1;

    @ApiModelProperty(value = "自定义2")
    private String customField2;

    @ApiModelProperty(value = "自定义3")
    private String customField3;


    @ApiModelProperty(value = "分配-批次号")
    private String distributionBatchCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "分配-生产时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime distributionMaterialProductionDate;

    @ApiModelProperty(value = "分配-自定义1")
    private String distributionCustomField1;

    @ApiModelProperty(value = "分配-自定义2")
    private String distributionCustomField2;

    @ApiModelProperty(value = "分配-自定义3")
    private String distributionCustomField3;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "箱编号")
    private String boxNumber;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
