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
 * 客户备案表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerRecond对象", description="客户备案表")
public class CustomerRecond extends Model<CustomerRecond> {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "备案时间")
    private LocalDateTime recondTime;

    @ApiModelProperty(value = "备案人")
    private String reconder;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "供应商id")
    private Integer customerId;

    @ApiModelProperty(value = "供应商名称")
    private String customerName;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

    @ApiModelProperty(value = "备案号")
    private String recondNo;

    @ApiModelProperty(value = "供应链公司备案号")
    private String chainCompanyRecondNo;

    @ApiModelProperty(value = "供应商备案号")
    private String customerRecondNo;

    @ApiModelProperty(value = "备案有效结束时间")
    private LocalDateTime recondEndTime;

    @ApiModelProperty(value = "备案税局")
    private String recondTaxStation;

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
