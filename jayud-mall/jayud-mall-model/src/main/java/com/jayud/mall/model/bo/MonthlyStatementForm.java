package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class MonthlyStatementForm {

    @ApiModelProperty(value = "月结时间(yyyy-MM-dd HH:mm:ss)", position = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 1, format="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "月结时间不能为空")
    private LocalDateTime monthlyStatement;
}