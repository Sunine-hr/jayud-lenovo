package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfferInfoDateVO {

    /*报价*/
    @ApiModelProperty(value = "报价-自增加id")
    private Long id;

    @ApiModelProperty(value = "报价-报价模板id(quotation_template id)")
    private Integer qie;

    @ApiModelProperty(value = "报价-开船日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "报价-截单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "报价-截仓日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime jcTime;

    @ApiModelProperty(value = "报价-截亏仓日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime jkcTime;

    @ApiModelProperty(value = "预计到达时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDateTime estimatedTime;

}
