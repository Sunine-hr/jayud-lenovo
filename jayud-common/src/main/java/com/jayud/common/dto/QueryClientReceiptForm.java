package com.jayud.common.dto;

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
public class QueryClientReceiptForm extends SysBaseEntity {


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


    //公司
    private List<String> orgIds;

    //是否在这个体系内
    private Boolean isCharge;

    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;


}
