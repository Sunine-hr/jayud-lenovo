package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 结算条款费用明细
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeeListVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "结算方案条款Id")
    private Integer feeId;

    @ApiModelProperty(value = "结算方案Id")
    private Integer feesId;

    @ApiModelProperty(value = "应收账期方式（现结，账期，月结）")
    private String arFeeType;

    @ApiModelProperty(value = "应收账期天数")
    private Integer arFeeDay;

    @ApiModelProperty(value = "应收起计日（报关当天，付款当天，结算当天）")
    private String beginDate;

    @ApiModelProperty(value = "月结方式(当月，次月，第三月)")
    private String monthType;

    @ApiModelProperty(value = "结算日")
    private Integer balanceDay;

    @ApiModelProperty(value = "结账日")
    private Integer accountDay;

    @ApiModelProperty(value = "应收对象")
    private String receTo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;


    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

}
