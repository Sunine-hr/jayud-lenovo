package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 付款单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
public class DispatchForm {

    @ApiModelProperty(value = "调度id")
    private Integer id;

    @ApiModelProperty(value = "出库单id集合")
    private List<Integer> shipIds;

    @ApiModelProperty(value = "提验货id集合")
    private List<Integer> checkIds;

    @ApiModelProperty(value = "发票id集合")
    private List<Integer> taxInvoiceIds;

}
