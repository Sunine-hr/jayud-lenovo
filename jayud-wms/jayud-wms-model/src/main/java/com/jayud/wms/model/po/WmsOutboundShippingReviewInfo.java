package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * WmsOutboundShippingReviewInfo 实体类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wms-出库发运复核对象", description="wms-出库发运复核")
public class WmsOutboundShippingReviewInfo extends SysBaseEntity {


    @ApiModelProperty(value = "出库通知单号")
    private String noticeOrderNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "发运复核订单号")
    private String shippingReviewOrderNumber;

    @ApiModelProperty(value = "主订单号")
    private String mainOrder;

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

    @ApiModelProperty(value = "单据类型(1待复核,2复核中,3已复核)")
    private String documentType;

    @ApiModelProperty(value = "订单来源(1ERP下发,2EMS下发,3手工创建)")
    private String orderSourceType;

    @ApiModelProperty(value = "发运复核状态(1待复核，2复核中，3已复核)")
    private String orderStatusType;

    @ApiModelProperty(value = "外部订单号1")
    private String externalOrderNumberFirst;

    @ApiModelProperty(value = "外部订单号2")
    private String externalOrderNumberSecond;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "合计数量")
    private BigDecimal allCount;

    @ApiModelProperty(value = "合计重量")
    private BigDecimal allHeight;

    @ApiModelProperty(value = "合计体积")
    private BigDecimal allVolume;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "车牌号")
    private String carBarnd;

    @ApiModelProperty(value = "车型")
    private String carModel;

    @ApiModelProperty(value = "司机名称")
    private String carDriver;

    @ApiModelProperty(value = "联系方式")
    private String carRelation;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "作业人员id")
    private String operatorsId;

    @ApiModelProperty(value = "作业人员名称")
    private String operatorsName;

    @ApiModelProperty(value = "确认人员id")
    private Long comfirmId;

    @ApiModelProperty(value = "确认人员名称")
    private String comfirmName;

    @ApiModelProperty(value = "确认时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date comfirmTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


    @TableField(exist = false)
    @ApiModelProperty(value = "单据类型文本")
    private String documentType_text;

    @TableField(exist = false)
    @ApiModelProperty(value = "订单来源文本")
    private String orderSourceType_text;

    @TableField(exist = false)
    @ApiModelProperty(value = "发运复核状态文本")
    private String orderStatusType_text;

    @TableField(exist = false)
    @ApiModelProperty(value = "复核时间")
    private List<String> theDeliveryTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "作业人员id集合")
    private List<Long> operatorsIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "仓库id集合")
    private List<String> warehouseIdList;

}
