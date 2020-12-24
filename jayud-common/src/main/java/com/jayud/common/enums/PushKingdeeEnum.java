package com.jayud.common.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 推送金蝶，日志状态枚举定义
 */
@Getter
@AllArgsConstructor
public enum PushKingdeeEnum {

    //云报关->金蝶，推送步骤
    STEP1("1","已调用云报关推送接口，创建推送日志"),
    STEP2("2","已发送数据到kafak(topic)"),
    STEP3("3","已消费kafka数据(topic offset)"),
    STEP4("4","已调用财务推送接口"),
    STEP5("5","已成功推送到金蝶云星空")
    ;

    @ApiModelProperty(value = "代码", position = 1)
    private String code;

    @ApiModelProperty(value = "消息", position = 2)
    private String msg;
}
