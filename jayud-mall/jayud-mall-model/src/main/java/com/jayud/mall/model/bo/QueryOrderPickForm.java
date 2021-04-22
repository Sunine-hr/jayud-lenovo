package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryOrderPickForm extends BasePageForm {

    //提货地址

    //提货日期
    @ApiModelProperty(value = "提货时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime pickTime;

    //截仓日期
    @ApiModelProperty(value = "截仓日期", position = 26)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 26, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime jcTime;

    //集货仓库
    @ApiModelProperty(value = "集货仓库代码", position = 23)
    private String shippingWarehouseCode;

    //关键字(订单编号、提货单号)
    @ApiModelProperty(value = "关键字(订单编号、提货单号)")
    private String keyword;

    @ApiModelProperty(value = "提货状态(1未提货 2正在提货 3已提货 4已到仓)")
    private Integer pickStatus;
}
