package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class AddFeeLadderForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "结算条款ID")
    private String feeId;

    @ApiModelProperty(value = "阶梯开始")
    private BigDecimal ladderMin;

    @ApiModelProperty(value = "阶梯结束")
    private BigDecimal ladderMax;

    @ApiModelProperty(value = "阶梯类型(服务费，账期费）")
    private String ladderType;

    @ApiModelProperty(value = "代理费率")
    private BigDecimal proxyFee;

    @ApiModelProperty(value = "账期费率")
    private BigDecimal insterest;

    @ApiModelProperty(value = "备注")
    private String remark;

}
