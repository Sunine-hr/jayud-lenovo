package com.jayud.oms.model.bo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 银行存款
 * </p>
 *
 * @author wh
 * @since 2021-11-10
 */
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@ApiModel(value="BankAccount对象", description="银行账户信息")
@Data
public class AddBankAccountFrom {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "开户行")
    private String accountBank;

    @ApiModelProperty(value = "开户行编号")
    private String num;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "币别类别")
    private String currencyCode;

    @ApiModelProperty(value = "收付款方式")
    private String paymentMethod;

    @ApiModelProperty(value = "交换行号")
    private String exchangeLineNum;

    @ApiModelProperty(value = "托收合同号")
    private String collectionContractNo;

    @ApiModelProperty(value = "状态（0 禁用 1启用 2删除）")
    private Integer status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "类型(1:客户,2:供应商)")
    private Integer type;


}
