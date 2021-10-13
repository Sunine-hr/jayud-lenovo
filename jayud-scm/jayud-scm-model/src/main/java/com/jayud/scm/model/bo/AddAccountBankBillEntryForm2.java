package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 水单明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class AddAccountBankBillEntryForm2 {

    @ApiModelProperty(value = "水单主表ID")
    private Integer bankBillId;


    @ApiModelProperty(value = "应收款明细集合")
    List<AddAccountBankBillEntryForm> addAccountBankBillEntryForms;

}
