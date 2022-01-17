package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 费用名称表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BOtherCostItem对象", description="费用名称表")
public class BOtherCostItem extends Model<BOtherCostItem> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "费用名称代码")
    private String itemCode;

    @ApiModelProperty(value = "费用名称")
    private String itemName;

    @ApiModelProperty(value = "最高承担金额")
    private BigDecimal itemLimitAmt;

    @ApiModelProperty(value = "报价")
    private BigDecimal itemOfferAmt;

    @ApiModelProperty(value = "自营税点")
    private BigDecimal itemSalesTax;

    @ApiModelProperty(value = "代理税点")
    private BigDecimal itemProxyTax;

    @ApiModelProperty(value = "承担方（客户承担，供应商，我司）")
    private String cdf;

    @ApiModelProperty(value = "备注")
    private String remark;

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
