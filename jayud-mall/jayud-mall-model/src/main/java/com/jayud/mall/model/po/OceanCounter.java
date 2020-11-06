package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 提单对应货柜信息
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanCounter对象", description="提单对应货柜信息")
public class OceanCounter extends Model<OceanCounter> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增加ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜型(cabinet_type idcode)")
    private String cabinetCode;

    @ApiModelProperty(value = "总体积")
    private Double volume;

    @ApiModelProperty(value = "费用")
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency_info 表id)")
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Long obId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
