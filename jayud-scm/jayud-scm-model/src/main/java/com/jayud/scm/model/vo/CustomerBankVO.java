package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CustomerBankVO {

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

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

}
