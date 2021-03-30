package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryWorkBillForm extends BasePageForm {


    @ApiModelProperty(value = "创建人(后台用户id)(system_user id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer createId;

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

    //状态
    @ApiModelProperty(value = "状态(1进行中 2已结单 3待评价 4已关闭)", position = 6)
    @JSONField(ordinal = 6)
    private Integer status;

    @ApiModelProperty(value = "操作人(后台用户、用户id)(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private Long operator;

}
