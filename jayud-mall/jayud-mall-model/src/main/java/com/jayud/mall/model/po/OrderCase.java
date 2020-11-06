package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单对应箱号信息
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderCase对象", description="订单对应箱号信息")
public class OrderCase extends Model<OrderCase> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info.id)")
    private Integer orderId;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = "FAB箱号")
    private String fabNo;

    @ApiModelProperty(value = "客户测量的长度，单位cm")
    private Double asnLength;

    @ApiModelProperty(value = "客户测量的重量，单位kg")
    private Double asnWidth;

    @ApiModelProperty(value = "客户测量的高度，单位cm")
    private Double asnHeight;

    @ApiModelProperty(value = "客户测量的重量，单位kg")
    private Double asnWeight;

    @ApiModelProperty(value = "预报长宽高计算得到的体积，单位m³")
    private Double asnVolume;

    @ApiModelProperty(value = "客户填写时间")
    private LocalDateTime asnWeighDate;

    @ApiModelProperty(value = "仓库测量的长度，单位cm")
    private Double wmsLength;

    @ApiModelProperty(value = "仓库测量的高度，单位cm")
    private Double wmsHeight;

    @ApiModelProperty(value = "仓库测量的宽度，单位cm")
    private Double wmsWidth;

    @ApiModelProperty(value = "仓库测量的重量，单位kg")
    private Double wmsWeight;

    @ApiModelProperty(value = "仓库计量长宽高得到的体积，单位m³")
    private Double wmsVolume;

    @ApiModelProperty(value = "仓库测量时间")
    private LocalDateTime wmsWeighDate;

    @ApiModelProperty(value = "最终确认重量，单位kg")
    private Double confirmLength;

    @ApiModelProperty(value = "最终确认高度，单位cm")
    private Double confirmHeight;

    @ApiModelProperty(value = "最终确认宽度，单位cm")
    private Double confirmWidth;

    @ApiModelProperty(value = "最终确认重量，单位cm")
    private Double confirmWeight;

    @ApiModelProperty(value = "最终确认体积，单位 m³")
    private Double confirmVolume;

    @ApiModelProperty(value = "最终确定时间")
    private LocalDateTime confirmWeighDate;

    @ApiModelProperty(value = "是否已确认（0,1）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
