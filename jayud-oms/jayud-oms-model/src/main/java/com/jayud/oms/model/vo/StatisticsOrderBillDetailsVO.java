package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class StatisticsOrderBillDetailsVO extends Model<StatisticsOrderBillDetailsVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单创建时间")
    private String createTime;

    @ApiModelProperty(value = "结算单位")
    private String customerName;

    @ApiModelProperty(value = "结算单位code")
    private String customerCode;

    @ApiModelProperty(value = "法人主体id")
    private Long legalId;

    @ApiModelProperty(value = "法人主体名称")
    private String legalName;

    @ApiModelProperty(value = "结算币种本币金额")
    private BigDecimal localMoney;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "总计核销金额")
    private BigDecimal heiXiaoLocalAmount;

    @ApiModelProperty(value = "总计短款金额")
    private BigDecimal shortLocalAmount;


}
