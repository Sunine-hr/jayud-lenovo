package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
public class WarehouseGoodsForm extends Model<WarehouseGoodsForm> {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "预计到达时间")
    private LocalDateTime estimatedArrivalTime;

    @ApiModelProperty(value = "预计出库时间")
    private LocalDateTime expectedDeliveryTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "附件地址（多个以逗号隔开）")
    private String filePath;

    @ApiModelProperty(value = "附件名称（多个以逗号隔开）")
    private String fileName;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> takeFiles;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "商品类型 1为入库  2为出库")
    private Integer type;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;

    //实际信息
    @ApiModelProperty(value = "板数")
    private Integer sjBoardNumber;

    @ApiModelProperty(value = "件数")
    private Integer sjNumber;

    @ApiModelProperty(value = "pcs")
    private Integer sjPcs;

    @ApiModelProperty(value = "重量")
    private Double sjWeight;

    @ApiModelProperty(value = "体积")
    private Double sjVolume;
}
