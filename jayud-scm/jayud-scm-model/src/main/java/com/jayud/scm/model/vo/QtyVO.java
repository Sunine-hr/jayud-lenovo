package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QtyVO {

    @ApiModelProperty(value = "委托单总数量")
    private BigDecimal bookingQty;

    @ApiModelProperty(value = "入库单总数量")
    private BigDecimal receivingQty;

}
