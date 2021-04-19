package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "创建箱号的参数")
public class CreateOrderCaseForm {

    //总箱数
    @ApiModelProperty(value = "总箱数", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "总箱数不能为空")
    @Min(value = 1,message = "总箱数大于等于1")
    private Integer cartons;

    //每箱重量
    @ApiModelProperty(value = "每箱重量", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "重量不能为空")
    private BigDecimal weight;

    //长
    @ApiModelProperty(value = "长", position = 3)
    @JSONField(ordinal = 3)
    @NotNull(message = "长不能为空")
    private BigDecimal length;

    //宽
    @ApiModelProperty(value = "宽", position = 4)
    @JSONField(ordinal = 4)
    @NotNull(message = "宽不能为空")
    private BigDecimal width;

    //高
    @ApiModelProperty(value = "高", position = 5)
    @JSONField(ordinal = 5)
    @NotNull(message = "高不能为空")
    private BigDecimal height;

}
