package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddCustomerAccountRelListVO {

    @ApiModelProperty(value = "角色集合")
    private List<Map<Long,String>> roles;

    @ApiModelProperty(value = "所客户公司集合")
    private List<Map<Long,String>> customerInfos;

    @ApiModelProperty(value = "所属上级集合")
    private List<Map<Long,String>> departCharges;
}
