package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询报价参数")
public class QueryOfferInfoForm extends BasePageForm{

    @ApiModelProperty(value = "报价名", position = 1)
    @JSONField(ordinal = 1)
    private String names;

    @ApiModelProperty(value = "开船日期", position = 2)
    @JSONField(ordinal = 2, format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "截单日期", position = 3)
    @JSONField(ordinal = 3, format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "国家代码", position = 4)
    @JSONField(ordinal = 4)
    private String countryCode;

}
