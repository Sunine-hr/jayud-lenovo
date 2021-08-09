package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCustomerNameForm {

    @ApiModelProperty(value = "编号")
    private String customerNo;

    @ApiModelProperty(value = "名称")
    @NotNull(message = "客户名称不为空")
    private String customerName;

    @ApiModelProperty(value = "统一信用代码")
    private String tax_no;




}
