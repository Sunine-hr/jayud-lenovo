package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InitComboxStrVO {

    @ApiModelProperty(value = "code隐藏值")
    private String code;

    @ApiModelProperty(value = "显示值")
    private String name;

    @ApiModelProperty(value = "显示其他值,比如合同的剩余时效")
    private String note;

    @ApiModelProperty(value = "id值")
    private Long id;

}
