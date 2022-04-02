package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.wms.model.bo.WmsReceiptAppendForm;
import com.jayud.wms.model.enums.ReceiptNoticeStatusEnum;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Receipt 实体类
 *
 * @author jyd
 * @since 2021-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "收货单对象", description = "收货单")
public class ReceiptVO extends SysBaseEntity {


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

    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "供应商id(基础客户表)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商(基础客户表)")
    private String supplier;

    @ApiModelProperty(value = "订单来源值(1:手工创建,,2:MES下发,3:ERP下发 <字典>)")
    private Integer orderSourceCode;

    @ApiModelProperty(value = "订单来源(字典)")
    private String orderSource;

    @ApiModelProperty(value = "计划收货时间（通知单带过来）")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date plannedReceivingTime;

    @ApiModelProperty(value = "预期数量")
    private Double totalNum;

    @ApiModelProperty(value = "预期重量")
    private Double totalWeight;

    @ApiModelProperty(value = "预期体积")
    private Double totalVolume;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "实收重量")
    private Double actualWeight;

    @ApiModelProperty(value = "实收体积")
    private Double actualVolume;

    @ApiModelProperty(value = "收货人")
    private String receiver;

    @ApiModelProperty(value = "订单状态（1：待收货：2：部分收货，3：完全收货，4：整单撤销）")
    private Integer status;

    @ApiModelProperty(value = "收货时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivingTime;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "是否直接上架")
    private Boolean isPutShelf;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "流程标志")
    private String processFlag;

    @ApiModelProperty(value = "质检单号")
    private String qcNo;

    @ApiModelProperty(value = "物料信息")
    private List<MaterialVO> materialForms;

    @ApiModelProperty(value = "物料sn信息")
    private List<MaterialSnVO> materialSnForms;

    @ApiModelProperty(value = "收货单附加服务表对象")
    private List<WmsReceiptAppendVO> wmsReceiptAppendForms;


    @ApiModelProperty(value = "订单状态明细 1：待收货：2：部分收货，3：完全收货，4：整单撤销 5：待上架, 6:已上架）")
    private String statusDetails;

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

    @ApiModelProperty(value = "客户code")
    private String clientCode;

    @ApiModelProperty(value = "入仓号集合")
    private String inWarehouseNumberList;

    @ApiModelProperty(value = "单据类型")
    private String orderType;

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDetails = ReceiptStatusEnum.getDesc(status);
    }

    public void calculationStatus() {
        if (this.actualNum.compareTo(this.totalNum) >= 0) {
            this.status = ReceiptStatusEnum.THREE.getCode();
        }
        if (this.actualNum.compareTo(this.totalNum) < 0) {
            this.status = ReceiptStatusEnum.TWO.getCode();
        }
        if (this.actualNum.compareTo(0.0) == 0) {
            this.status = ReceiptStatusEnum.ONE.getCode();
        }
    }
}
