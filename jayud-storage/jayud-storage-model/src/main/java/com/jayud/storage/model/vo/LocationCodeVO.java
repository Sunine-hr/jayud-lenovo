package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 商品区域货架表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class LocationCodeVO extends Model<LocationCodeVO> {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "库位编码")
    private String locationCode;

    @ApiModelProperty(value = "货架层")
    private Integer shelvesLine;

    @ApiModelProperty(value = "货架列数")
    private Integer shelvesColumn;

    @ApiModelProperty(value = "货架类型")
    private String shelvesType;

    @ApiModelProperty(value = "长")
    private Double length;

    @ApiModelProperty(value = "宽")
    private Double width;

    @ApiModelProperty(value = "高")
    private Double height;

    @ApiModelProperty(value = "最大体积范围")
    private String maximumVolume;

    @ApiModelProperty(value = "最大受重")
    private Double maximumWeight;

    @ApiModelProperty(value = "sku种类上限")
    private Integer categoryUpperLimit;

    @ApiModelProperty(value = "二维码地址")
    private String qrUrl;

    public void setMaximumVolume(Double maximumVolume) {
        this.maximumVolume = this.height+"*"+this.length+"*"+this.height;
    }
}
