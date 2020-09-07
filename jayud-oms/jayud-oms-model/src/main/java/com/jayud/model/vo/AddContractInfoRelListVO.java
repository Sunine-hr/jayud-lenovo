package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddContractInfoRelListVO {

    @ApiModelProperty(value = "客户名称集合")
    private List<Map<String,String>> customerInfos;

    @ApiModelProperty(value = "法人主体集合")
    private List<Map<String,String>> legalEntitys;

}
