package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TradingRecordCZForm {

    @ApiModelProperty(value = "客户ID(customer id)", position = 1)
    @JSONField(ordinal = 1)
    private Long customerId;

    //交易流水号
    @ApiModelProperty(value = "交易流水号", position = 2)
    @TableField("serialNumber")
    @JSONField(ordinal = 2)
    private String serialNumber;

    //状态
    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核不通过)", position = 3)
    @JSONField(ordinal = 3)
    private String status;

    //币种
    @ApiModelProperty(value = "币种(currency_info id)", position = 4)
    @JSONField(ordinal = 4)
    private Long cid;

}
