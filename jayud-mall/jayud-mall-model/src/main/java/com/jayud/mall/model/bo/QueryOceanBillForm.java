package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询提单form")
public class QueryOceanBillForm extends BasePageForm{

    @ApiModelProperty(value = "提单号(供应商提供)", position = 1)
    private String orderId;

    @ApiModelProperty(value = "开船日期", position = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;
    @ApiModelProperty(value = "开船日期-开始时间", position = 3)
    private String sailTimeStart;
    @ApiModelProperty(value = "开船日期-结束时间", position = 4)
    private String sailTimeEnd;

    @ApiModelProperty(value = "供应商代码(supplier_info supplier_code)", position = 5)
    private String supplierCode;

    @ApiModelProperty(value = "目的港口(harbour_info idcdode)", position = 6)
    private String endCode;

}
