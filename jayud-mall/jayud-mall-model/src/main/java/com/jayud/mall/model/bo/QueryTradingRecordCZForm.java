package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryTradingRecordCZForm extends BasePageForm {

    //充值单号
    @ApiModelProperty(value = "交易单号(充值单号)", position = 1)
    @JSONField(ordinal = 1)
    private String tradingNo;

    //充值账户
    @ApiModelProperty(value = "充值账户", position = 2)
    @JSONField(ordinal = 2)
    private String accountName;

    //交易流水号
    @ApiModelProperty(value = "交易流水号", position = 3)
    @TableField("serialNumber")
    @JSONField(ordinal = 3)
    private String serialNumber;

    //状态
    @ApiModelProperty(value = "状态(0待审核 1审核通过 2审核不通过)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

}
