package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class QueryReceiptForm extends SysBaseEntity {


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

    @ApiModelProperty(value = "订单来源code(字典)")
    private String orderSourceCode;

    @ApiModelProperty(value = "订单来源(字典)")
    private String orderSource;


    @ApiModelProperty(value = "实收体积")
    private Double actualVolume;

    @ApiModelProperty(value = "收货人")
    private String receiver;

    @ApiModelProperty(value = "订单状态（1：待收货：2：部分收货，3：完全收货，4：整单撤销）")
    private Integer status;



    @ApiModelProperty(value = "预到货时间")
    private List<String> arrivalTime;

    @ApiModelProperty(value = "收货时间")
    private List<String> timeOfReceipt;

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

    @ApiModelProperty(value = "单据类型")
    private String orderType;

    //仓库id
    private List<String> warehouseIdList;

}
