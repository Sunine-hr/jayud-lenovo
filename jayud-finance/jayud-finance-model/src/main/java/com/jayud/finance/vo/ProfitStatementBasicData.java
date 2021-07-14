package com.jayud.finance.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.po.ProfitStatement;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 利润报表
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ProfitStatement对象", description = "利润报表")
public class ProfitStatementBasicData extends Model<ProfitStatementBasicData> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "费用主键")
    private Long costId;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "录用费用金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "录用费用金额币种code")
    private String currencyCode;

    @ApiModelProperty(value = "录用费用金额币种名称")
    private String currencyName;

    @ApiModelProperty(value = "录用费用本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "出账本币金额")
    private BigDecimal localMoney;

    @ApiModelProperty(value = "法人名称")
    private String legalName;

    @ApiModelProperty(value = "法人id")
    private Long legalId;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "结算单位名称")
    private String unitName;

    @ApiModelProperty(value = "子订单费用类型")
    private String subType;

    @ApiModelProperty(value = "是否合拼费用")
    private Boolean isSumToMain;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String department;

    @ApiModelProperty(value = "是否内部往来")
    private Boolean isInternal;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务类型code")
    private String bizCode;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "业务员名称")
    private String  bizUname;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "费用类型(1:应付,2:应收)")
    private Integer type;


    public ProfitStatement convert(Class<ProfitStatement> clazz) {
        ProfitStatement profitStatement = ConvertUtil.convert(this, clazz);
        profitStatement.setIsMain(isSumToMain);
        return profitStatement;
    }
}
