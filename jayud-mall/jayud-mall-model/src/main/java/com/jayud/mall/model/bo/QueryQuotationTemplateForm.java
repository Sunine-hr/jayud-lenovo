package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询报价模板参数")
public class QueryQuotationTemplateForm extends BasePageForm{

    @ApiModelProperty(value = "报价名")
    private String names;

    @ApiModelProperty(value = "开船日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;
    @ApiModelProperty(value = "开船日期-开始时间")
    private String sailTimeStart;
    @ApiModelProperty(value = "开船日期-结束时间")
    private String sailTimeEnd;

    @ApiModelProperty(value = "截单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cutOffTime;
    @ApiModelProperty(value = "截单日期-开始时间")
    private String cutOffTimeStart;
    @ApiModelProperty(value = "截单日期-结束时间")
    private String cutOffTimeEnd;

    @ApiModelProperty(value = "目的港")
    private String destinationPort;

}
