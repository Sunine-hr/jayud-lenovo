package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusinessLogVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "操作人id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "操作人name(system_user name)")
    private String userName;

    @ApiModelProperty(value = "业务表tb")
    private String businessTb;

    @ApiModelProperty(value = "业务表(中文)name")
    private String businessName;

    @ApiModelProperty(value = "业务操作(insert update delete)")
    private String businessOperation;

    @ApiModelProperty(value = "操作前(text)")
    private String operationFront;

    @ApiModelProperty(value = "操作后(text)")
    private String operationAfter;

}
