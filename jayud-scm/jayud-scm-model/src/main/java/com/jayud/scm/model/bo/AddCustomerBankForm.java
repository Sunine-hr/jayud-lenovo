package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class AddCustomerBankForm {

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

    @ApiModelProperty(value = "备注")
    private String remark;


}
