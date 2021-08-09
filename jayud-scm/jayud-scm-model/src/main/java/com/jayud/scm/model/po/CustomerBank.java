package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户银行表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerBank对象", description="客户银行表")
public class CustomerBank extends Model<CustomerBank> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
      private Integer id;

    @ApiModelProperty(value = "客户主表ID")
    private Integer customerId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行地址")
    private String bankAddress;

    @ApiModelProperty(value = "区域【境内 境外】")
    private String bankArea;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "银行账号")
    private String accountNo;

    @ApiModelProperty(value = "swift code")
    private String swiftCode;

    @ApiModelProperty(value = "银行编号【非 swift code】")
    private String bankCode;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "是否默认值")
    private Integer isDefault;

    @ApiModelProperty(value = "在线id")
    private String onlId;

    @ApiModelProperty(value = "erp是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "是否禁用")
    private Integer isDisabled;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

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
