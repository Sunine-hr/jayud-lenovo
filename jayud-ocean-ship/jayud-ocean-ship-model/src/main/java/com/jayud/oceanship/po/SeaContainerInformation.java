package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 货柜信息表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SeaContainerInformation对象", description="货柜信息表")
public class SeaContainerInformation extends Model<SeaContainerInformation> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "海运订单号")
    private String seaOrderNo;

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

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
