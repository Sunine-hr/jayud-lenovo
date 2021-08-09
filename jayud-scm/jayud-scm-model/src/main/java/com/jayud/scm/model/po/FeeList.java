package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 结算条款费用明细
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FeeList对象", description="结算条款费用明细")
public class FeeList extends Model<FeeList> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer feeId;

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

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
