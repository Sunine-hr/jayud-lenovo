package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库报表 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
public class DeliveryReportVO extends SysBaseEntity {


    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "分配物料明细id")
    private Long allocationId;

    @ApiModelProperty(value = "任务明细号")
    private String taskDetailNumber;

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

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "库位ID")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "所属仓库货架Code")
    private String shelfCode;

    @ApiModelProperty(value = "容器id")
    private Long containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "容器名称")
    private String containerName;

    @ApiModelProperty(value = "下架库位id")
    private Long offshelfLocationId;

    @ApiModelProperty(value = "下架库位编号")
    private String offshelfLocationCode;

    @ApiModelProperty(value = "下架库位名称")
    private String offshelfLocationName;

    @ApiModelProperty(value = "待拣货下架数量")
    private BigDecimal waitOffshelfAccount;

    @ApiModelProperty(value = "实际货下架数量")
    private BigDecimal realOffshelfAccount;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "拣货下架状态(1 待拣货下架,2 拣货下架中,3 拣货下架完成)")
    private Integer status;

    @ApiModelProperty(value = "送达工作站id")
    private Long deliveryWorkstationId;

    @ApiModelProperty(value = "送达工作站编号")
    private String deliveryWorkstationCode;

    @ApiModelProperty(value = "送达工作站名称")
    private String deliveryWorkstationName;

    @ApiModelProperty(value = "送达播种位id")
    private Long deliverySowingId;

    @ApiModelProperty(value = "送达播种位编号")
    private String deliverySowingCode;

    @ApiModelProperty(value = "送达播种位名称")
    private String deliverySowingName;

    @ApiModelProperty(value = "是否下发-机器人")
    private Boolean isIssue;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "接收时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

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

    @ApiModelProperty(value = "排序")
    private int sorts;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "出库通知单号")
    @TableField(exist = false)
    private String noticOrderNumber;

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


    @TableField(exist = false)
    private List<String> detailNumberList;

    @TableField(exist = false)
    private List<String> orgIds;

    @TableField(exist = false)
    private Boolean isCharge;

    @TableField(exist = false)
    private List<String> owerIdList;

    @TableField(exist = false)
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "拣货状态文本")
    @TableField(exist = false)
    private String status_text;

    @ApiModelProperty(value = "是否结束流程")
    @TableField(exist = false)
    private Boolean isEnd;





}
