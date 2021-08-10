package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 费用计算公式表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeesComboxVO {

    @ApiModelProperty(value = "费用类别")
    private String feeAlias;

    @ApiModelProperty(value = "费用公式集合")
    private List<FeesVO> feesVOS;

}
