package com.jayud.oms.model.vo;

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
 * 统计基础费用
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StatisticsOrderBaseCost extends Model<StatisticsOrderBaseCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "实际产生业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "状态(0-草稿 1-审核通过 2-审核驳回)")
    private Integer status;

    @ApiModelProperty(value = "订单创建时间")
    private String orderCreatedTime;

    @ApiModelProperty(value = "是否出账 1-已出账 0-未出账")
    private String isBill;

    @ApiModelProperty(value = "是否汇总到主订单")
    private Boolean isSumToMain;

    @ApiModelProperty(value = "录用费用类型")
    private String subType;

    @ApiModelProperty(value = "子订单法人主体名字")
    private String legalName;

    @ApiModelProperty(value = "子订单法人主体id")
    private Long legalId;

    @ApiModelProperty(value = "是否内部费用")
    private Boolean isInternal;

    @ApiModelProperty(value = "订单结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "订单结算单位")
    private String unitName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
