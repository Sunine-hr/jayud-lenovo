package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 公司银行账户
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class BBanksVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行编号")
    private String bankCode;

    @ApiModelProperty(value = "swift_code")
    private String swiftCode;

    @ApiModelProperty(value = "银行凭证代码（维度可核算项目）")
    private String accountNumber;

    @ApiModelProperty(value = "开户行支行名称")
    private String branch;

    @ApiModelProperty(value = "银行地址")
    private String address;

    @ApiModelProperty(value = "联系人")
    private String manager;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "银行账号")
    private String accountNo;

    @ApiModelProperty(value = "账户币别")
    private String currencyName;

    @ApiModelProperty(value = "区域【境内 境外】")
    private String bankArea;

    @ApiModelProperty(value = "备注")
    private String remark;

}
