package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderCaseReceiptForm {

    @ApiModelProperty(value = "箱号")
    @NotBlank(message = "箱号不能为空")
    private String cartonNo;

    @ApiModelProperty(value = "仓库测量的长度，单位cm")
    @NotNull(message = "长度不能为空")
    private BigDecimal wmsLength;

    @ApiModelProperty(value = "仓库测量的宽度，单位cm")
    @NotNull(message = "宽度不能为空")
    private BigDecimal wmsWidth;

    @ApiModelProperty(value = "仓库测量的高度，单位cm")
    @NotNull(message = "高度不能为空")
    private BigDecimal wmsHeight;

    @ApiModelProperty(value = "仓库测量的重量，单位kg")
    @NotNull(message = "重量不能为空")
    private BigDecimal wmsWeight;

    @ApiModelProperty(value = "仓库计量长宽高得到的体积，单位m³")
    private BigDecimal wmsVolume;

    @ApiModelProperty(value = "仓库测量时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime wmsWeighDate;

}
