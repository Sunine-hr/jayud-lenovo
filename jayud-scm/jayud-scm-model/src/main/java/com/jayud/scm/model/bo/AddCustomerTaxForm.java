package com.jayud.scm.model.bo;

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
 * 客户开票资料
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddCustomerTaxForm {


    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "开票抬头")
    private String taxName;

    @ApiModelProperty(value = "开票地址")
    private String taxAddress;

    @ApiModelProperty(value = "开票电话")
    private String taxTel;

    @ApiModelProperty(value = "开票银行")
    private String taxBank;

    @ApiModelProperty(value = "开票银行账号")
    private String taxBankNo;

    @ApiModelProperty(value = "是否默认")
    private Integer isDefaultValue;

    @ApiModelProperty(value = "税务编号，纳税识别号，统一信用号码")
    private String taxNo;

    @ApiModelProperty(value = "备注")
    private String remark;

}
