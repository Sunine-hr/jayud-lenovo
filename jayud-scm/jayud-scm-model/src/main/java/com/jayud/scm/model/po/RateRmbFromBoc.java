package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 中行汇率表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="RateRmbFromBoc对象", description="中行汇率表")
public class RateRmbFromBoc extends Model<RateRmbFromBoc> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "现汇买入价")
    private BigDecimal buyingRate;

    @ApiModelProperty(value = "现钞买入价")
    private BigDecimal cashBuyingRate;

    @ApiModelProperty(value = "现汇卖出价")
    private BigDecimal sellingRate;

    @ApiModelProperty(value = "现钞卖出价")
    private BigDecimal cashSellingRate;

    @ApiModelProperty(value = "中间价")
    private BigDecimal middleRate;

    @ApiModelProperty(value = "折算价")
    private BigDecimal bocRate;

    @ApiModelProperty(value = "汇率时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
