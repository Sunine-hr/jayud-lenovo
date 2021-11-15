package com.jayud.oms.model.bo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.oms.model.po.BankAccount;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BankAccount对象", description="银行账户信息")
@Data
public class BankAccountFrom extends BasePageForm  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "类型(1:客户,2:供应商)")
    private Integer type;


}
