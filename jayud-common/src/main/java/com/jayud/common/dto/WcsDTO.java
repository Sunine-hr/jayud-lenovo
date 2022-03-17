package com.jayud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

/**
 * WCS(仓储控制系统) AGV自动化导向车(Automated Guided Vehicle),返回的结果对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WcsDTO {

    @ApiModelProperty(value = "请求编号返回，形成一一对应")
    private String reqCode;

    /**
     * code结果码定义:
     * “0”    成功
     * “1”    参数相关的错误
     * “6”    重复发送， 上层系统不需要重发
     * “99”    其他未知错误，调用失败后，可以重试
     * “100”    该任务不存在，上层系统不需重发，需要人工介入处理
     */
    @ApiModelProperty(value = "返回编号， “0” ：成功， 1~N：失败")
    private String code;

    @ApiModelProperty(value = "“0” ：成功;1~N：其他的详细描述")
    private String message;

    @ApiModelProperty(value = "返回数据对象")
    private T data;

}
