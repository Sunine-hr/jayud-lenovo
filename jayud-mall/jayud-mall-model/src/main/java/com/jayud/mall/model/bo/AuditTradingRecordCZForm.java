package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AuditTradingRecordCZForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "审核充值记录id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核不通过)", position = 9)
    @JSONField(ordinal = 9)
    private String status;

    @ApiModelProperty(value = "金额", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 6)
    @JSONField(ordinal = 6)
    private Long cid;

    @ApiModelProperty(value = "交易流水号", position = 7)
    @TableField("serialNumber")
    @JSONField(ordinal = 7)
    private String serialNumber;

    @ApiModelProperty(value = "交易备注", position = 10)
    @JSONField(ordinal = 10)
    private String remark;



}
