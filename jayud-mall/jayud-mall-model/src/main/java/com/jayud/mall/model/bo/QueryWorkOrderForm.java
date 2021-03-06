package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class QueryWorkOrderForm extends BasePageForm {

    @ApiModelProperty(value = "创建人(客户id customer id, 用户id system_user id)", position = 1)
    @JSONField(ordinal = 1)
    private Integer creator;

    @ApiModelProperty(value = "工单业务类型(1订单工单 2提单工单)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "工单业务类型必填")
    private Integer businessType;

    @ApiModelProperty(value = "工单编号", position = 3)
    @JSONField(ordinal = 3)
    private String workNo;

    @ApiModelProperty(value = "提交时间Start", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTimeStart;

    @ApiModelProperty(value = "提交时间End", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submissionTimeEnd;

    @ApiModelProperty(value = "关键字(工单编号、问题内容)", position = 5)
    @JSONField(ordinal = 5)
    private String keyword;

    //状态
    @ApiModelProperty(value = "状态(1进行中 2已结单 3待评价 4已关闭)", position = 6)
    @JSONField(ordinal = 6)
    private Integer status;

    @ApiModelProperty(value = "操作人(后台用户、用户id)(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private Long operator;



}