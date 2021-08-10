package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 结算条款阶梯价
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddFeeLadderSettingForm {

    @ApiModelProperty(value = "结算方案Id")
    private Integer feeId;

    @ApiModelProperty(value = "阶梯价设置集合")
    private List<AddFeeLadderForm> addFeeLadderForms;

}
