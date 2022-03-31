package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.wms.model.enums.ReceiptNoticeStatusEnum;
import com.jayud.wms.model.enums.SowingResultsStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * ReceiptNotice 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ReceiptNotice对象", description = "收货通知单")
public class ReceiptNoticeVO extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    private String ower;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "单据类型(存中文值)")
    @NotBlank(message = "请选择单据类型")
    private String documentType;

    @ApiModelProperty(value = "单据类型编号(字典)")
    @NotBlank(message = "请选择单据类型")
    private String documentTypeCode;

    @ApiModelProperty(value = "订单来源值(1:手工创建,,2:MES下发,3:ERP下发 <字典>)")
    private Integer orderSourceCode;

    @ApiModelProperty(value = "订单来源(字典)")
    private String orderSource;

    @ApiModelProperty(value = "外部订单号1")
    private String externalOrderNumOne;

    @ApiModelProperty(value = "外部订单号2")
    private String externalOrderNumTwo;

    @ApiModelProperty(value = "供应商id(基础客户表)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商(基础客户表)")
    private String supplier;

    @ApiModelProperty(value = "预计到货时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estimatedArrivalTime;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "合计数量")
    private Double totalNum;

    @ApiModelProperty(value = "合计重量")
    private Double totalWeight;

    @ApiModelProperty(value = "合计体积")
    private Double totalVolume;

    @ApiModelProperty(value = "确认人")
    private String confirmedBy;

    @ApiModelProperty(value = "订单状态")
    private Integer status;

    @ApiModelProperty(value = "订单状态明细")
    private String statusDetails;

    @ApiModelProperty(value = "确认时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date confirmationTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "是否删除")
//    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "物料信息")
    private List<NoticeMaterialVO> noticeMaterialForms;

    @ApiModelProperty(value = "物料sn信息")
    private List<NoticeSnMaterialVO> noticeSnMaterialForms;


    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "流程标志")
    private String processFlag;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNumber;
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "车牌")
    private String carBarnd;

    @ApiModelProperty(value = "车型")
    private String carModel;

    @ApiModelProperty(value = "司机")
    private String carDriver;

    @ApiModelProperty(value = "联系方式")
    private String carRelation;

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDetails = ReceiptNoticeStatusEnum.getIntegerDesc(status);
    }


}
