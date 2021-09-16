package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.enums.UnitEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.NumberUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.vo.InitComboxVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用模板详情
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderCostTemplateInfo对象", description = "费用模板详情")
public class OrderCostTemplateInfoDTO extends Model<OrderCostTemplateInfoDTO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "费用名称code")
    private String costCode;

    @ApiModelProperty(value = "费用类别(作业环节)")
    private Long costTypeId;

    @ApiModelProperty(value = "费用类型")
    private Long costGenreId;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量")
    private BigDecimal number;

    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "币种")
    private String currencyName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "作业环节")
    private List<InitComboxVO> categorys;


    public String getCurrencyName() {
        return this.currency;
    }

    public void checkAdd() {
        if (StringUtils.isEmpty(this.costCode)) {
            throw new JayudBizException(500, "请输入费用名称");
        }
        if (this.costTypeId == null) {
            throw new JayudBizException(500, "请输入费用类别");
        }
        if (this.costGenreId == null) {
            throw new JayudBizException(500, "请输入费用类型");
        }
        if (StringUtils.isEmpty(this.unit)) {
            throw new JayudBizException(500, "请输入单位");
        }
        if (this.unitPrice == null) {
            throw new JayudBizException(500, "请输入单价");
        }
        if (this.number == null) {
            throw new JayudBizException(500, "请输入数量");
        }
        if (this.currencyCode == null) {
            throw new JayudBizException(500, "请输入币种");
        }

        if (UnitEnum.CTNS.name().equals(this.unit)
                || UnitEnum.PCS.name().equals(this.unit)
                || UnitEnum.BILL.name().equals(this.unit)) {
            if (!NumberUtils.isIntegerValue(this.number)) {
                throw new JayudBizException("数量请输入整数（单位:CTNS/PCS/BILL)");
            }
        }
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
