package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品区域货架表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class WarehouseAreaShelvesLocationVO extends Model<WarehouseAreaShelvesLocationVO> {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "仓库代码")
    private String code;

    @ApiModelProperty(value = "货架id")
    private String shelvesId;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "区域代码")
    private String areaCode;

    @ApiModelProperty(value = "货架名称")
    private String shelvesName;

    @ApiModelProperty(value = "货架层")
    private Integer shelvesLine;

    @ApiModelProperty(value = "货架列数")
    private Integer shelvesColumn;

    @ApiModelProperty(value = "货架类型")
    private Long shelvesType;

    @ApiModelProperty(value = "货架类型名字")
    private String shelvesTypeName;

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

    @ApiModelProperty(value = "sku种类上限")
    private List<String> kuCodeList;

    public void setMaximumVolume() {
        if(this.height == null){
            this.height = 0.0;
        }
        if(this.length == null){
            this.length = 0.0;
        }
        if(this.width == null){
            this.width = 0.0;
        }
        this.maximumVolume = this.height+"*"+this.length+"*"+this.width;
    }
}
