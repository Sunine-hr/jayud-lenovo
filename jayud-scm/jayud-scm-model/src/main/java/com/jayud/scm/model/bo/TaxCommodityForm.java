package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 税收分类表单
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TaxCommodityForm {

    @ApiModelProperty(value = "税收分类ID集合")
    private List<Integer> id;

    @ApiModelProperty(value = "分类代码")
    private String taxCode;

    @ApiModelProperty(value = "分类名称")
    private String taxItemName;

    @ApiModelProperty(value = "类型名称")
    private String taxClassName;


}
