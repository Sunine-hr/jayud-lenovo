package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询报价模板参数")
public class QueryQuotationTemplateForm extends BasePageForm{

    @ApiModelProperty(value = "报价名", position = 1)
    private String names;

    @ApiModelProperty(value = "开船日期", position = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "开船日期-开始时间", position = 3)
    private String sailTimeStart;

    @ApiModelProperty(value = "开船日期-结束时间", position = 4)
    private String sailTimeEnd;

    @ApiModelProperty(value = "截单日期", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;

    @ApiModelProperty(value = "截单日期-开始时间", position = 6)
    private String cutOffTimeStart;

    @ApiModelProperty(value = "截单日期-结束时间", position = 7)
    private String cutOffTimeEnd;

    @ApiModelProperty(value = "目的港", position = 8)
    private String destinationPort;

}
