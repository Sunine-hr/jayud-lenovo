package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryWorkOrderForm extends BasePageForm {

    @ApiModelProperty(value = "创建人(客户用户、客户id)、提交账号(customer id)", position = 1)
    @JSONField(ordinal = 1)
    private Integer customerId;

    @ApiModelProperty(value = "工单编号", position = 2)
    @JSONField(ordinal = 2)
    private String workNo;

    @ApiModelProperty(value = "提交时间Start", position = 3)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 3, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTimeStart;

    @ApiModelProperty(value = "提交时间End", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTimeEnd;

    @ApiModelProperty(value = "关键字(工单编号、问题内容)", position = 5)
    @JSONField(ordinal = 5)
    private String keyword;

}