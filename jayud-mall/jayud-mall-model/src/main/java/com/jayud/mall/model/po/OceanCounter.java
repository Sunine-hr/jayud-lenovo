package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "自增加ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "柜号", position = 2)
    @JSONField(ordinal = 2)
    private String cntrNo;

    @ApiModelProperty(value = "柜型(cabinet_type id_code)", position = 3)
    @JSONField(ordinal = 3)
    private String cabinetCode;

    @ApiModelProperty(value = "总体积", position = 4)
    @JSONField(ordinal = 4)
    private Double volume;

    @ApiModelProperty(value = "费用", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal cost;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer cid;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 7)
    @TableField(value = "`status`")
    @JSONField(ordinal = 7)
    private String status;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 8)
    @JSONField(ordinal = 8)
    private Long obId;

    @ApiModelProperty(value = "创建时间", position = 9)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 9, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
