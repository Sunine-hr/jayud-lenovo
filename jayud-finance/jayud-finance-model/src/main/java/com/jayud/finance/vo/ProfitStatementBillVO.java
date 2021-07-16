package com.jayud.finance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.BigDecimalUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.bo.QueryProfitStatementForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 利润报表账单
 * </p>
 *
 * @author chuanmei
 * @since 2021-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfitStatementBillVO extends Model<ProfitStatementBillVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应收账单")
    List<ProfitStatementBillDetailsVO> reBills = new ArrayList<>();

    @ApiModelProperty("应付账单")
    List<ProfitStatementBillDetailsVO> payBills = new ArrayList<>();
}
