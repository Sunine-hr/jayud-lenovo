package com.jayud.storage.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 仓储商品信息表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseGoodsLocationVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "商品对应的订单号")
    private String orderNo;

    @ApiModelProperty(value = "商品对应的订单id")
    private Long orderId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "板数")
    private Integer boardNumber;

    @ApiModelProperty(value = "商品批次号")
    private Integer commodityBatchNumber;

    @ApiModelProperty(value = "件数")
    private Integer number;

    @ApiModelProperty(value = "pcs")
    private Integer pcs;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "商品类型 1为入库  2为出库")
    private Integer type;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "预计出库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedDeliveryTime;

    @ApiModelProperty(value = "默认库位")
    private String defaultLocation;

    @ApiModelProperty(value = "出库库位以及对应的商品和数量")
    private List<GoodsLocationRecordFormVO> goodsLocationRecordForms;

}
