package com.jayud.wms.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.wms.model.enums.ReceiptNoticeStatusEnum;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * ReceiptNotice 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ReceiptNotice对象", description = "收货通知单")
public class ReceiptNoticeForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    @NotNull(message = "请选择仓库")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    @NotBlank(message = "请选择仓库")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    @NotNull(message = "请选择货主")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    @NotBlank(message = "请选择货主")
    private String ower;

    @ApiModelProperty(value = "收货通知单号")
//    @NotBlank(message = "请填写收货通知单号")
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
    private LocalDate estimatedArrivalTime;

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

    @ApiModelProperty(value = "确认时间")
    private LocalDate confirmationTime;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "物料信息")
    private List<NoticeMaterialForm> noticeMaterialForms;

//    @ApiModelProperty(value = "物料sn信息")
//    private List<NoticeSnMaterialForm> noticeSnMaterialForms;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNumber;
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "车牌")
    private String carBarnd;

    @ApiModelProperty(value = "车型")
    private String carMmodel;

    @ApiModelProperty(value = "司机")
    private String carDriver;

    @ApiModelProperty(value = "联系方式")
    private String carRelation;

    public void setStatus(Integer status) {
        if (status == null) {
            this.status = ReceiptNoticeStatusEnum.CREATE.getCode();
        } else {
            this.status = status;
        }

    }

    /**
     * 计算合计数量
     */
    public void calculateTotalQuantity() {
        Double totalNum = 0.0;
        Double totalWeight = 0.0;
        Double totalVolume = 0.0;
        for (NoticeMaterialForm noticeMaterialForm : noticeMaterialForms) {
            totalNum += noticeMaterialForm.getNum();
            totalWeight += noticeMaterialForm.getNum() * noticeMaterialForm.getWeight().doubleValue();
            totalVolume += noticeMaterialForm.getNum() * noticeMaterialForm.getVolume().doubleValue();
        }
        this.totalNum = totalNum;
        this.totalWeight = totalWeight;
        this.totalVolume = totalVolume;
    }
}
