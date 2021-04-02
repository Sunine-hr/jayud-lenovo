package com.jayud.oceanship.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 货柜信息表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-25
 */
@Data
@ApiModel(value="SeaContainerInformation对象", description="货柜信息表")
public class SeaContainerInformationVO extends Model<SeaContainerInformationVO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "截补料id")
    private Long seaRepId;

    @ApiModelProperty(value = "截补料单号")
    private String seaRepNo;

    @ApiModelProperty(value = "柜型尺寸")
    private String cabinetName;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "件数")
    private Integer platNumber;

    @ApiModelProperty(value = "包装")
    private String packing;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
