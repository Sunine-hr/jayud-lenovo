package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 运输管理表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Transport对象", description="运输管理表")
public class Transport extends Model<Transport> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运输单号", position = 2)
    @JSONField(ordinal = 2)
    private String transportNo;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "追踪号", position = 4)
    @JSONField(ordinal = 4)
    private String trackingNo;

    @ApiModelProperty(value = "总重量", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal weight;

    @ApiModelProperty(value = "总体积", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal volume;

    @ApiModelProperty(value = "费用,金额", position = 7)
    @JSONField(ordinal = 7)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 8)
    @JSONField(ordinal = 8)
    private Integer cid;

    @ApiModelProperty(value = "运输状态(1在途 2已送达)", position = 9)
    @JSONField(ordinal = 9)
    private Integer transportStatus;

    @ApiModelProperty(value = "创建日期", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
