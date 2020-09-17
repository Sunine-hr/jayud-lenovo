package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报关进程字段VO
 *
 * @author william
 * @description
 * @Date: 2020-09-09 18:02
 */
@Data
public class DeclarationTraceVO {
    @ApiModelProperty(value = "操作名称")
    private String cname;
    @ApiModelProperty(value = "处理时间")
    private LocalDateTime process_dt;
}
