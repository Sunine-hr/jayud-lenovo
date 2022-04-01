package com.jayud.oms.order.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
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
public class AddWarehouseGoodsForm extends Model<AddWarehouseGoodsForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "出仓号")
    @NotNull(message = "出仓号不为空")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "入仓号")
    @NotNull(message = "入仓号不为空")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "商品对应的订单号")
    private String orderNo;

    @ApiModelProperty(value = "商品对应的订单id")
    private Long orderId;

    @ApiModelProperty(value = "货物名称")
    private String name;

    @ApiModelProperty(value = "货品编号")
    private String itemNo;

    @ApiModelProperty(value = "拣货下架库位")
    private String pickingOffShelfLocation;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;



    @ApiModelProperty(value = "板数")
    private Integer boardNumber;

    @ApiModelProperty(value = "商品批次号")
    private Integer commodityBatchNumber;

    @ApiModelProperty(value = "数量")
    private Integer number;

    @ApiModelProperty(value = "单位")
    private Integer unit;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "生产日期")
    private String manufactureTime;

    @ApiModelProperty(value = "卡板编号")
    private String cardNumber;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
